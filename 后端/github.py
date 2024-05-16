import re
import requests
from requests import Session
from requests.packages.urllib3.exceptions import InsecureRequestWarning
requests.packages.urllib3.disable_warnings(InsecureRequestWarning)
def get_auth_token(session: Session) -> str:
    resp = session.get('https://github.com/login',verify=False)
    if resp.status_code != 200:
        print("请求失败，请稍后再试！")
        exit(0)
    login_html = resp.content.decode()
    auth_token = re.findall(r'name="authenticity_token" value="(.*?)"', login_html)[0]
    return auth_token
def do_login(session: Session,token,account,password):
    global resp
    post_data = {
        "commit": "Sign in",
        "authenticity_token": token,
        "login": account,
        "password": password,  # 登录密码，为了个人账号安全我这里不是真实密码
        "webauthn-conditional": "undefined",
        "javascript-support": "true",
        "webauthn-support": "supported",
        "webauthn-iuvpaa-support": "supported",
        "return_to": "https://github.com/login"
    }
    resp = session.post(url='https://github.com/session', data=post_data)
    if resp.status_code != 200:
        print("请求失败，请检查参数！")
    else:
        print("请求/session 成功！")
def chk_login_status(session: Session):
    resp = session.get('https://github.com/hjc1985')
    html_content = resp.content
    res = re.findall(r'<title>(.+?)(GitHub)?</title>', html_content.decode('utf-8'))
    try:
        end_str = res[0][1]
    except IndexError:
        end_str = ""
    if end_str == "":
        # 个人主页的title内容如果结尾没有GitHub，说明登录成功
        return "200"
    else:
        return "500"
    with open("github-profile.html", "wb") as f:
        f.write(html_content)
def test_githubconnect(account,password):
    session = requests.session()
    session.headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0'
    }
    token = get_auth_token(session)
    do_login(session,token,account,password)
    message = chk_login_status(session)
    return message

