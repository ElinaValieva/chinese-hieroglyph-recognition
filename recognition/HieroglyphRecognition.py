import numpy as np
from keras.engine.saving import model_from_json

import recognition.HieroglyphNNModel as nn
from util import Util

image_size = 100


def recognize(image_path):
    test_dataset = Util.load_image(image_path, image_size)
    try:
        json_file = open("../recognition/mnist_model.json", "r")
        loaded_model_json = json_file.read()
        json_file.close()
        loaded_model = model_from_json(loaded_model_json)
        loaded_model.load_weights("../recognition/mnist_model.h5")
        loaded_model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])
    except FileNotFoundError:
        print('File not found error. Try to train NN')
        nn.train()

    prediction = loaded_model.predict(test_dataset)
    result = np.argmax(prediction)
    print('Recognize hieroglyph: ' + str(result))
    return result
