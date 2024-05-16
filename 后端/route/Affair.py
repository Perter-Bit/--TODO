from flask import Blueprint
from flask_jwt_extended import jwt_required, get_jwt_identity
from sqlalchemy_serializer import SerializerMixin
from database.database import db
from flask import jsonify,request
from utils.return_message import return_response,return_data
from route.user import User
affair = Blueprint('affairs',__name__)
class Affair(db.Model,SerializerMixin):
    __tablename__ = 'affair'
    serialize_rules = ("-details","-users")
    affairid = db.Column (db.Integer, primary_key = True, autoincrement = True)
    type = db.Column(db.Integer, nullable=False)
    message = db.Column(db.String(50),nullable = False)
    imageId = db.Column(db.Integer, nullable=False)
    time = db.Column(db.Integer, nullable=True)
    # 在Affair表中添加外键，绑定User表的主键id（表名不区分大小写）
    userid = db.Column(db.Integer, db.ForeignKey('user.userid'))
    details = db.relationship('Detail', backref='affairs')
@affair.route('/add',methods=["POST"])
@jwt_required()
def add():
    message = request.form.get("message")
    userid = get_jwt_identity()
    if  Affair.query.filter(Affair.message == message,Affair.userid==userid).first():
        response = return_response("代办不能重复建立!",500)
    else:
        type = int(request.form.get("type"))
        time = int(request.form.get("time"))
        imageId = int(request.form.get("imageId"))
        affair = Affair(userid = userid,type=type,imageId=imageId,message=message,time=time)
        db.session.add(affair)
        # 需要提交事务给数据库
        db.session.commit()
        response = return_response("代办添加成功!",200)
    return response
@affair.route('/query',methods=["GET"])
@jwt_required()
def query():
    userid = get_jwt_identity()
    #根据用户id查询其对应的所有事务
    user = User.query.filter(User.userid == userid).first()
    list = []
    for affair in user.affairs:
        list.append(affair.to_dict())
    if list == []:
        response = return_data(None,"您暂时未有代办",500)
    else:
        response = return_data(list,"信息查询成功",200)
        print(list)
    return response
@affair.route('/delete',methods=["GET"])
@jwt_required()
def delete():
    userid = get_jwt_identity()
    message = request.args.get("message")
    print(userid)
    affair = Affair.query.filter(Affair.message==message,Affair.userid==userid).first()
    for detail in affair.details:
        db.session.delete(detail)
    db.session.delete(affair)
    db.session.commit()
    response = return_response("删除成功",200)
    return response
