import numpy as np
import recognition.HieroglyphNNModel as nn
from recognition import Util

image_size = 100


def recognize(image_path):
    test_dataset = Util.load_image(image_path, image_size)
    try:
        model = Util.load_nn_model()
    except FileNotFoundError:
        print('File not found error. Try to train NN')
        nn.train()

    prediction = model.predict(test_dataset)
    result = np.argmax(prediction)
    print('Recognize hieroglyph: ' + str(result))
    return result
