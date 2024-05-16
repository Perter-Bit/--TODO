from flask import Flask, request, jsonify, make_response
from flask_jwt_extended import jwt_required, get_jwt_identity, JWTManager, create_access_token
from  database.database import db
import database.database_config as database_config
from route.News import news
from route.user import User,user
from route.Affair import affair,Affair
from route.Detail import detail,Detail
from route.Subscript import subscript
from datetime import datetime, timedelta
from qqlogin import login_qq
from utils.inserti_into_db import insert_user,insert_affair,insert_truuser
from utils.return_message import return_response
app = Flask(__name__)
jwt = JWTManager(app)
app.config['JWT_SECRET_KEY'] = "123456"
app.config['JWT_ACCESS_TOKEN_EXPIRES'] = timedelta(hours=1)
app.config.from_object(database_config)
db.init_app(app)
# with app.app_context():
#     db.drop_all()
#     db.create_all()
#     insert_truuser()
#     insert_affair()
#     insert_user()
from github import test_githubconnect
@app.route('/')
def main():
    return "这里是主页"
@app.route('/login', methods=['GET'])
def login():
    username = request.args.get('username', None)
    password = request.args.get('password', None)
    user = User.query.filter(User.username == username, User.password == password).first()
    if user:
        access_token = create_access_token(identity=user.userid);
        response = return_response("登录成功",200,access_token)
    else:
        response = return_response("用户或密码错误",500)
    return response
@app.route('/register',methods=['POST'])
def register():
    name = request.form.get('username')
    password = request.form.get('password')
    email = request.form.get('email')
    user = User.query.filter(User.username == name).first()
    if user == None:
        user1 = User(username=name, password=password, email=email)
        db.session.add(user1)
        # 需要提交事务给数据库
        db.session.commit()
        response = return_response("用户添加成功",200)
    else:
        response = return_response("用户名不能重复",500)
    return response
@app.route('/loginBygithub',methods=["GET"])
def login_bygithub():
    #获取请求中的github账号和密码
    account = request.args.get("account")
    password = request.args.get("password")
    #根据账号查找对应的用户
    user = User.query.filter(User.account == account).first()
    if user is None:
        # 未找到绑定账号
        response = make_response(jsonify({'message': '您暂时未绑定github账号,请绑定后登录使用'}))
        response.status_code = 200
        return response
    else:
        message = test_githubconnect(account, password)
        if message == "200":
            user = User.query.filter(User.account == account).first()
            #返回token
            access_token = create_access_token(identity=user.userid)
            response = return_response("登录成功", 200, access_token)
        else:
            response = return_response("github账号或者密码错误",500)
    return response
@app.route('/loginByqq',methods=["GET"])
def loginbyqq():
    account = request.args.get("account")+"@qq.com"
    print(account)
    user = User.query.filter(User.email == account).first()
    if user is None:
        response = return_response("邮箱输入错误,请重新输入", 500)
    else:
        message = login_qq(account)
        response = return_response(message, 200)
    return response
@app.route('/loginverige',methods=["GET"])
def verify():
    verify = request.args.get("verify")
    if verify != "12345678":
        response = return_response("验证码输入错误",500)
    else:
        password = request.args.get("password")
        email = request.args.get("account")+"@qq.com"
        user = User.query.filter(User.email == email).first()
        user.password = password;
        db.session.commit();
        response = return_response("密码修改成功",200)
    return response
#注册蓝图
app.register_blueprint(user,url_prefix='/user')
app.register_blueprint(affair,url_prefix="/affair")
app.register_blueprint(detail,url_prefix="/detail")
app.register_blueprint(subscript,url_prefix="/subscript")
app.register_blueprint(news,url_prefix="/news")
if __name__ == '__main__':
    app.run()

