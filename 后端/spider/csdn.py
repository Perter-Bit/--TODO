from urllib import request
from lxml import etree

from utils.return_message import return_data


def CSDN(name):
    url = "https://blog.csdn.net/"+name
    html = request.urlopen(request.Request(url)).read().decode('utf-8')
    htmls = etree.HTML(html)
    text = htmls.xpath('//article//h4/text()')
    describe = htmls.xpath('//article/a/div[1]/div[1]/div[2]/text()')
    if describe != [] and text !=[]:
        return text[0]+describe[0]
    elif describe!=[] and text == []:
        return describe[0]
    elif describe == [] and text != []:
        return text[0]
def geturl(name):
    url = "https://blog.csdn.net/" + name
    html = request.urlopen(request.Request(url)).read().decode('utf-8')
    htmls = etree.HTML(html)
    new_url = htmls.xpath('//article[1]/a/@href')[0]
    text = htmls.xpath('//article//h4/text()')[0]
    describe = htmls.xpath('//article/a/div[1]/div[1]/div[2]/text()')[0]
    img = htmls.xpath('//article[1]/a//img/@src')
    if img == []:
        #没有图片使用头像
        img = htmls.xpath('//div[@class="user-profile-avatar"]/img/@src')[0]
        print(new_url,text,describe,img)
    else:
        img = img[0]
        print(new_url, text, describe, img)
    dict = {"url":new_url,"title":text,"describe":describe,"img_url":img}
    return dict
# dict = geturl("weixin_63974376")
# print(dict.get("url"))

