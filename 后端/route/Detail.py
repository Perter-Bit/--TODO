from datetime import datetime
from flask import Blueprint
from flask_jwt_extended import jwt_required, get_jwt_identity
from sqlalchemy_serializer import SerializerMixin
from database.database import db
from flask import jsonify,request
from route.Affair import Affair
from itertools import groupby
from utils.time import time_convert,conver_back
from route.user import User
from utils.return_message import return_response, return_data
from utils.time import time_convert
detail = Blueprint('details',__name__)
class Detail(db.Model,SerializerMixin):
    __tablename__ = 'detail'
    detailId = db.Column (db.Integer, primary_key = True, autoincrement = True)
    date = db.Column(db.Date, nullable=False)
    time = db.Column(db.BigInteger,nullable = False)
    is_over = db.Column(db.Boolean,nullable=False)
    affairid = db.Column(db.Integer, db.ForeignKey('affair.affairid'))
@detail.route('/add',methods = ['GET'])
@jwt_required()
def add():
    message = request.args.get("message")
    affair = Affair.query.filter(Affair.message == message).first();
    affairid = affair.affairid
    date = request.args.get('date')
    date = time_convert(int(date))
    time = int(request.args.get('time'))
    over = request.args.get('is_over')
    if over == "0":
       is_over=False
    else:
        is_over = True
    datail1 = Detail(affairid = affairid,date=date,time=time,is_over=is_over)
    db.session.add(datail1)
    db.session.commit()
    response  = return_response("计时成功",200)
    return response
@detail.route('/query',methods = ['GET'])
@jwt_required()
def query():
    userid = get_jwt_identity()
    message = request.args.get("message")
    affair = Affair.query.filter(Affair.message == message,Affair.userid==userid).first();
    print(affair)
    list = []
    for detail in affair.details:
        detail_dict = {
            'detailId': detail.detailId,
            'affaidId': detail.affairid,
            'date': conver_back(detail.date),
            'time': detail.time,
            'is_over': detail.is_over
        }
        list.append(detail_dict)
    response = return_data(list, "查询成功", 200)
    return response
@detail.route('/pie',methods = ['POST'])
@jwt_required()
def pie():
    userid = get_jwt_identity()
    user1 = User.query.filter(User.userid == userid).first()
    dict={}
    sum = 0
    for affair in user1.affairs:
        count = 0
        for detail in affair.details:
            count +=detail.time
        if count!=0:
            sum+=count
            dict.update({count:affair.message})
            count = 0
    return_dict = {}
    if sum != 0:
        for (key,values) in dict.items():
            key = key / sum
            return_dict.update({key:values})
    response = return_data(return_dict, "成功", 200)
    return response
@detail.route('/statistics',methods = ['POST'])
@jwt_required()
def statistics():
    userid = get_jwt_identity()
    user1 = User.query.filter(User.userid == userid).first()
    day = int(request.form.get("day"))
    sum_all = 0
    sum_today = 0
    hour_all = 0
    hour_today = 0
    abandon = 0
    for affair in user1.affairs:
        for detail in affair.details:
            print(detail)
            hour_all +=detail.time
            if detail.date.day == day:
                hour_today+=detail.time
                sum_today+=1
                if detail.is_over == 0:
                    abandon+=1
            sum_all+=1
    dict = {"累计":sum_all,"累计时长":hour_all,"日均时长":int(hour_all/day),
            "今日次数":sum_today,
            "今日时长":hour_today,
            "放弃次数":abandon};
    print(dict)
    response = return_data(dict,"成功",200)
    return response

@detail.route('/radar',methods = ['POST'])
@jwt_required()
def radar():
    userid = get_jwt_identity()
    user1 = User.query.filter(User.userid == userid).first()
    dict1 = {}
    for affair in user1.affairs:
        count1 = 0
        count2 = 0
        for detail in affair.details:
            if 1 <= detail.date.day <= 7:
                count1+=1
            elif 8<=detail.date.day <=14:
                count2+=1
            dict1.update({affair.message :count1,affair.message + "2":count2})
    response = return_data(dict1, "成功", 200)
    return response

@detail.route('/line',methods = ['POST'])
@jwt_required()
def line():
    userid = get_jwt_identity()
    user = User.query.get(userid)
    if user:
        all_details = Detail.query.join(Affair).filter(Affair.userid == userid).all()
        sorted_details = sorted(all_details, key=lambda x: x.date)
        # 然后使用itertools.groupby函数对排序后的结果进行分组
        grouped_details = {}
        for category, group in groupby(sorted_details, key=lambda x: x.date):
            grouped_details[category] = list(group)
    dict_new = {}
    for key, value in grouped_details.items():
        sum = 0
        for detaill in value:
            sum+=detaill.time
        date_str = key.strftime("%Y-%m-%d")
        dict_new.update({date_str:sum})
    print(dict_new)
    response = return_data(dict_new, "成功", 200)
    return response
