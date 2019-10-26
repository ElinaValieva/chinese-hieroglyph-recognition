from flask import Flask
from flask import request
import recognition.HieroglyphRecognition as recognitionModule

# Rest-full module with end-point

app = Flask(__name__)


@app.route('/', methods=["POST"])
def hello():
    try:
        if request.method == 'POST':
            image_path = request.json['path']
            result = recognitionModule.recognize(image_path)
            return str(result)
    except Exception:
        return 'Error. Check that your file exist.'


if __name__ == '__main__':
    app.run(port=8080)
