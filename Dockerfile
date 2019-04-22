FROM python:alpine3.7
ADD HieroglyphController.py /
RUN pip install flask
CMD [ "python", "./HieroglyphController.py" ]
