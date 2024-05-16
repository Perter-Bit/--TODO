import os
import cv2
#存放视频的地址
videos_src_path = 'D:/android_homework/video'
#存放图片的地址
videos_save_path = 'D:/android_homework/image'
#返回videos_src_path路径下包含的文件或文件夹名字的列表（所有视频的文件名），按字母顺序排序
videos = os.listdir(videos_src_path)
for each_video in videos:
    #获取每个视频的名称
    each_video_name, _ = each_video.split('.')
    #获取每个视频的完整路径
    each_video_full_path = os.path.join(videos_src_path, each_video)
    #读入视频
    cap = cv2.VideoCapture(each_video_full_path)
    #设置提取视频的第几帧(n=几，就提取第几帧)
    n=1
    i=1
    while i <= n:
        #提取视频帧，success为是否成功获取视频帧（true/false），第二个返回值为返回的视频帧
        success, frame = cap.read()
        i+=1
    if success == True:
        print(success)
        #存储视频帧，图片名与视频名一致
        cv2.imwrite(videos_save_path + each_video_name+".jpg", frame)