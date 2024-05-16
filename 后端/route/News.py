from flask import Blueprint, make_response
from flask_jwt_extended import jwt_required, get_jwt_identity
from sqlalchemy_serializer import SerializerMixin
from database.database import db
from route.user import User
from utils.return_message import return_data

news = Blueprint('news',__name__)
class News(db.Model,SerializerMixin):
    __tablename__ = 'news'
    newsId = db.Column (db.Integer, primary_key = True, autoincrement = True)
    url = db.Column(db.String(500),nullable = False)#网页url
    title = db.Column(db.String(50),nullable = False)
    describe = db.Column(db.String (500),nullable=False)
    img_url = db.Column(db.String(500),nullable=True)
    userid = db.Column(db.Integer, db.ForeignKey('user.userid'))
@news.route('/query',methods=["GET"])
@jwt_required()
def query():
    userid = get_jwt_identity()
    user = User.query.filter(User.userid == userid).first()
    list = []
    for sub in user.news:
        list.append(sub.to_dict())
    print(list)
    if list == []:
        response = return_data(None, "您暂时未有订阅更新", 500)
    else:
        response = return_data(list, "信息查询成功", 200)
    return response
