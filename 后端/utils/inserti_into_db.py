import random
from datetime import datetime, timedelta

from database.database import db
from route.Affair import Affair
from route.Detail import Detail
from route.user import User
names = ["Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace",
         "Hannah", "Ian", "Julia", "Kevin", "Linda", "Mike", "Nancy",
         "Olivia", "Paul", "Quinn", "Rachel", "Sam", "Tina"]
actions = [
    "跳舞",
    "唱歌",
    "练瑜伽",
    "健身",
    "写作",
    "画画",
    "阅读",
    "学习新技能",
    "散步",
    "跑步",
    "骑自行车",
    "游泳",
    "打篮球",
    "踢足球",
    "做饭",
    "拍照",
    "观看电影",
    "听音乐",
    "旅行",
    "写代码"
]
def generate_unique_name():
    # 如果名字列表为空，则返回 None
    if not names:
        return None
    # 随机选择一个名字
    selected_name = random.choice(names)
    # 从列表中移除已选择的名字
    names.remove(selected_name)
    return selected_name
def generate_unique_action():
    # 如果名字列表为空，则返回 None
    if not actions:
        return None
    # 随机选择一个名字
    selected_name = random.choice(actions)
    # 从列表中移除已选择的名字
    actions.remove(selected_name)
    return selected_name
def insert_truuser():
    for i in range(10):
        name = generate_unique_name()
        password = str(random.randint(1,10))+str(random.randint(1,10))+str(random.randint(1,10))
        email = str(random.randint(1,10))+str(random.randint(1,10))+str(random.randint(1,10))+"@qq.com"
        user= User(username=name,password=password,email=email)
        db.session.add(user)
    db.session.commit()
def insert_affair():
    for i in range(20):
        type = random.randint(0,1)
        message = generate_unique_action()
        imageId = random.randint(1,10)
        time = random.randint(0,60)
        userid = random.randint(1,10)
        affair = Affair(type = type,message=message,imageId=imageId,time=time,userid=userid)
        db.session.add(affair)
    db.session.commit()
def insert_user():
    # 定义起始日期和结束日期
    start_date = datetime(2024, 4, 1)
    end_date = datetime(2024, 4, 18)
    # 生成数据
    data = []
    for i in range(1000):
        #随机生成日期
        random_date = start_date + timedelta(days=random.randint(0, (end_date - start_date).days))
        #随机生成第五列的值（1 或 2）
        fifth_column = random.randint(1, 15)
        detail_data = Detail(date = random_date.strftime('%Y-%m-%d'),time = random.randint(1,60),is_over = random.randint(0,1),affairid = fifth_column)
        #生成数据行并添加到列表中
        db.session.add(detail_data)
    db.session.commit()
