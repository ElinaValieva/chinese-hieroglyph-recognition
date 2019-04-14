from keras.models import Sequential
from keras.layers import Dense, Conv2D, Flatten
from recognition import Util

count_categories = 12
count_epochs = 100
image_size = 100


def train():
    train_x = Util.generate_dataset('../train_data/', image_size)
    train_y = Util.generate_categories(count_categories)

    model = Sequential()
    model.add(
        Conv2D(128, (3, 3), activation='relu', input_shape=(1, image_size, image_size), data_format='channels_first'))
    model.add(Conv2D(64, kernel_size=3, activation='relu'))
    model.add(Flatten())
    model.add(Dense(12, activation='softmax'))

    model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

    model.fit(train_x, train_y,
              batch_size=count_categories,
              nb_epoch=count_epochs,
              verbose=1)

    Util.save_nn_model(model)
