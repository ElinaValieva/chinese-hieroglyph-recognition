import numpy as numpy
from keras.preprocessing import image
from keras.models import model_from_json
import recognition.HieroglyphNNModel as nn


def recognize(image_path):
    image_format = image.load_img(image_path, target_size=(28, 28), color_mode="grayscale")

    # Transform image to vector
    vector = image.img_to_array(image_format)

    # Normalization image
    vector = 255 - vector
    vector /= 255
    vector = numpy.expand_dims(vector, axis=1)

    try:
        # Loading model from json
        json_file = open("../recognition/mnist_model.json", "r")
        loaded_model_json = json_file.read()
        json_file.close()
        loaded_model = model_from_json(loaded_model_json)
        loaded_model.load_weights("../recognition/mnist_model.h5")
        loaded_model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])
    except FileNotFoundError:
        print('File not found error. Try to train NN')
        nn.train()

    # Prediction result of input
    prediction = loaded_model.predict(vector)
    result = numpy.argmax(prediction)
    return result
