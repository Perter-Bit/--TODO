import re
from flask import Blueprint, make_response
from flask_jwt_extended import jwt_required, get_jwt_identity
from sqlalchemy_serializer import SerializerMixin
from database.database import db
from flask import jsonify,request
from chat import getresponse
from route.News import News
from route.user import User
from spider.csdn import CSDN, geturl
from utils.return_message import return_response, return_data
subscript = Blueprint('subscripts',__name__)
class Subscript(db.Model,SerializerMixin):
    __tablename__ = 'Subscript'
    subid = db.Column (db.Integer, primary_key = True, autoincrement = True)
    #订阅名称
    name = db.Column(db.String(50),nullable = False)
    #订阅的描述
    message = db.Column(db.String(50),nullable = False)
    #订阅访问的url
    url = db.Column(db.String(30))
    #订阅当次的内容
    content = db.Column(db.String(1000),nullable=True)
    #外键约束
    userid = db.Column(db.Integer, db.ForeignKey('user.userid'))
    #订阅用户id
    username = db.Column(db.String(50),nullable=False)
@subscript.route('/add',methods=["GET"])
@jwt_required()
def add():
    userid = get_jwt_identity()
    name = request.args.get('name')
    message = request.args.get('message')
    back = getresponse("请帮我分析这段话:"+message + ",这句话中请求的网站名称和要访问的博主的名称是什么,请以查找的网站名称xxx、博主名称是xxx的形式是返回给我其他啥消息我也不要,不要提示语")
    print(back)
    split_text = back.split("，")
    # 提取网站名称和博主名称
    website_part = split_text[0]
    blogger_part = split_text[1]
    # 提取网站名称
    url= website_part.split("是")[1]
    # 提取博主名称
    username = blogger_part.split("是")[1].rstrip("。")
    print(url,username)
    if url == "CSDN" or url == 'csdn':
        content = CSDN(username)
    print(f"要读入数据库的信息如下:\n订阅名:{name},详细内容:{message},url:{url},第一次爬取内容:{content}")
    subscript = Subscript(name=name,message=message,url=url,content=content,userid=userid,username=username)
    db.session.add(subscript)
    db.session.commit()
    response = return_response("订阅成功",200)
    return response
@subscript.route('/query',methods=["GET"])
@jwt_required()
def query():
    userid = get_jwt_identity()
    user = User.query.filter(User.userid == userid).first()
    list = []
    for sub in user.subs:
        list.append(sub.to_dict())
    print(list)
    if list == []:
        response = return_data(None, "您暂时未有订阅", 500)
    else:
        response = return_data(list, "信息查询成功", 200)
    return response
@subscript.route('/Roulette',methods=["GET"])
@jwt_required()
def Roulette():
    userid = get_jwt_identity()
    username = request.args.get('username')
    print(username)
    subscript = Subscript.query.filter(Subscript.username == username,Subscript.userid == userid).first()
    if subscript.url == "CSDN" or subscript.url=="csdn":
        content_new = CSDN(subscript.username)
        if content_new != subscript.content:
            # 内容更新,继续爬虫
            dict = geturl(username)
            str1 = dict.get("title").replace(" ", "")
            str2 = dict.get("describe").replace(" ","")
            news = News(userid = userid,url=dict.get("url"),title=str1,describe = str2,img_url=dict.get("img_url"))
            subscript.content = content_new
            response = return_response("查询成功",200)
            db.session.add(news)
            db.session.commit()
            print(dict)
        else:
            print("老弟还没更新呢")
            response = return_response("查询成功",200)
    return response
