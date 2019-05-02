FROM python:alpine3.7
FROM gw000/keras:2.1.4-py3-tf-cpu
FROM tiangolo/uwsgi-nginx-flask:python3.7
ADD controller/HieroglyphController.py /
EXPOSE 8080
CMD [ "python", "controller/HieroglyphController.py" ]
