from datetime import datetime
def time_convert(time):
    time = time/1000  # 这里假设时间戳为整数型
    date_time = datetime.fromtimestamp(time)
    # 格式化日期时间对象为字符串
    formatted_date = date_time
    # 打印格式化后的日期字符串
    return formatted_date
def conver_back(date):
    datetime_object = datetime.combine(date, datetime.min.time())
    # 获取时间戳
    timestamp = datetime.timestamp(datetime_object)
    return timestamp
