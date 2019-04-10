FROM python:3
ADD HieroglyphController.py /
RUN pip install pystrich
CMD [ "python", "./HieroglyphController.py" ]