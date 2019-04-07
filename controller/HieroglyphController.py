from flask import Flask
from flask import request
import recognition.HieroglyphRecognition as recognitionModule

app = Flask(__name__)


@app.route('/', methods=["POST"])
def hello():
    if request.method == 'POST':
        image_path = request.json['path']
        result = recognitionModule.recognize(image_path)
        return str(result)


if __name__ == '__main__':
    app.run(port=8080)
