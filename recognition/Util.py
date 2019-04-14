import numpy as np

import glob as global_util
from PIL import Image
from keras.engine.saving import model_from_json
from keras.utils import np_utils


def generate_dataset(path, size):
    directory = global_util.glob(path + '*.png')
    counter = 1
    np_img = []
    for i in directory:
        img = Image.open(str(i))
        img = img.convert('L')
        if directory.index(i) == 0:
            np_img = np.array(img, float)
            continue
        np_img = np.append(np_img, np.array(img, float), 0)
        counter += 1
    dataset = np_img.reshape(counter, 1, size, size)
    dataset /= 255
    return dataset


def generate_categories(category_size):
    class_array = np.array(list(range(0, category_size)))
    return np_utils.to_categorical(class_array, category_size)


def save_nn_model(model):
    model_json = model.to_json()
    json_file = open("mnist_model.json", "w")
    json_file.write(model_json)
    json_file.close()
    model.save_weights("mnist_model.h5")


def load_image(path, size):
    img = Image.open(path)
    img = img.convert('L')
    np_img = np.array(img, float)
    np_img /= 255
    dataset = np_img.reshape(1, 1, size, size)
    return dataset


def load_nn_model():
    json_file = open("../recognition/mnist_model.json", "r")
    loaded_model_json = json_file.read()
    json_file.close()
    loaded_model = model_from_json(loaded_model_json)
    loaded_model.load_weights("../recognition/mnist_model.h5")
    loaded_model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])
    return loaded_model
