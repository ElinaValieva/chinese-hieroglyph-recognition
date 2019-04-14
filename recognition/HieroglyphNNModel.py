from keras.models import Sequential
from keras.layers import Dense, Conv2D, Flatten
from util import Util

count_epochs = 100
image_size = 100


# Module for creating neural network and training on dataset from 'train_data/..'

def train():
    train_x = Util.generate_dataset('../train_data/', image_size)
    count_categories = Util.get_categories_cnt()
    train_y = Util.generate_categories()

    model = Sequential()
    model.add(
        Conv2D(128, (3, 3), activation='relu', input_shape=(1, image_size, image_size), data_format='channels_first'))
    model.add(Conv2D(64, kernel_size=3, activation='relu'))
    model.add(Flatten())
    model.add(Dense(count_categories, activation='softmax'))

    model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

    model.fit(train_x, train_y,
              batch_size=count_categories,
              nb_epoch=count_epochs,
              verbose=1)

    Util.save_nn_model(model)
