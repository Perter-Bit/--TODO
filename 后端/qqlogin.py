import smtplib
import email.utils
from email.mime.text import MIMEText
def login_qq(account):
    message = MIMEText("您的验证码是:12345678")
    message['To'] = email.utils.formataddr(('TODO软件系统',account))
    message['From'] = email.utils.formataddr(('TODO', '1796755024@qq.com'))
    message['Subject'] = 'TODO软件系统登录'
    server = smtplib.SMTP_SSL('smtp.qq.com', 465)
    server.login('1796755024@qq.com','buteewuicymbjbfa')
    server.set_debuglevel(True)
    try:
        server.sendmail('1796755024@qq.com',[account],msg=message.as_string())
        return "邮箱发送成功,请及时接收"
    finally:
        server.quit()
