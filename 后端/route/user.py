from flask import Blueprint, make_response
from flask_jwt_extended import jwt_required, get_jwt_identity
from sqlalchemy_serializer import SerializerMixin
from database.database import db
from flask import jsonify,request
from chat import getresponse
from github import test_githubconnect
import re

from utils.return_message import return_data

user = Blueprint('users',__name__)
class User(db.Model,SerializerMixin):
    __tablename__ = 'user'
    serialize_rules = ("-affairs","--subs","--news")
    userid = db.Column (db.Integer, primary_key = True, autoincrement = True)
    username = db.Column(db.String(50),nullable = False)
    password = db.Column(db.String(50),nullable = False)
    email = db.Column(db.String (30))
    account = db.Column(db.String(30),nullable=True)
    #一对多的关系,其中User表是少的一方,User的实例可以通过affairs属性访问所有相关的affair,
    #Affair实例可以通过users获取affair
    affairs = db.relationship('Affair', backref='users')
    subs = db.relationship('Subscript',backref='users')
    news = db.relationship('News',backref='users')
@user.route('/bindgithub',methods=['POST'])
@jwt_required()
def bind_github():
    username = get_jwt_identity()
    user = User.query.filter(User.username==username)
    account = request.form.get("account")
    password = request.form.get("password")
    message = test_githubconnect(account, password)
    if message == 200:
        # 绑定成功
        user.account = account
        db.session.commit()
        response = make_response(jsonify({'message': "绑定成功"}))
        response.status_code = 200
    else:
        response = make_response(jsonify({'message': "绑定失败,账号或者密码不正确"}))
        response.status_code = 500
    return response
@user.route('/chat',methods=['GET'])
@jwt_required()
def chat():
    question = request.args.get("question")
    response = getresponse(question)
    return response
@user.route('/getname',methods=['GET'])
@jwt_required()
def get_name():
    userid = get_jwt_identity()
    user = User.query.filter(User.userid == userid).first()
    name = user.username
    return return_data(name,"",200)