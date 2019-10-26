import glob as global_util

import numpy as np
from PIL import Image
from keras.utils import np_utils

# Module for working with datasets, models

cnt_categories = 0
categories = []


def get_categories_cnt():
    return cnt_categories


def generate_dataset(path, size):
    global cnt_categories, categories
    loaded_path = '../' + path + '/'
    directory = global_util.glob(loaded_path + "*/")
    directory2 = global_util.glob(loaded_path + "*/*.png")
    counter = 1
    np_img = []
    for i in directory:
        for j in global_util.glob(i + '*.png'):
            img = Image.open(str(j))
            img = img.convert('L')
            name = int(i.replace(path, '').replace('\\', '').replace('../', ''))
            categories.append(name)
            if directory2.index(j) == 0:
                np_img = np.array(img, float)
                continue
            np_img = np.append(np_img, np.array(img, float), 0)
            counter += 1
    dataset = np_img.reshape(counter, 1, size, size)
    dataset /= 255
    cnt_categories = max(categories) + 1
    return dataset


def generate_categories():
    class_array = np.array(categories)
    return np_utils.to_categorical(class_array, cnt_categories)


def save_nn_model(model):
    model_json = model.to_json()
    json_file = open("../recognition/mnist_model.json", "w")
    json_file.write(model_json)
    json_file.close()
    model.save_weights("../recognition/mnist_model.h5")


def load_image(path, size):
    img = Image.open(path)
    img = img.convert('L')
    np_img = np.array(img, float)
    np_img /= 255
    dataset = np_img.reshape(1, 1, size, size)
    return dataset
