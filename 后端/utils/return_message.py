from flask import make_response, jsonify
def return_response(message,status=None,token=None):
    if token:
        response = make_response(jsonify({'token': token, 'message': message}))
    else:
        response = make_response(jsonify({'message':message}))
    if status:
        response.status_code = status
    return response
def return_data(data,message,status):
    response = make_response(jsonify({'data':data,"message":message}))
    response.status_code = status
    return response