from keras.layers import Dense, Conv2D, Flatten, MaxPooling2D, Dropout
from keras.models import Sequential

from util import Util

count_epochs = 50
image_size = 100


# Module for creating neural network and training on dataset from 'train_data/..'

def train():
    train_x = Util.generate_dataset('train', image_size)
    count_categories = Util.get_categories_cnt()
    train_y = Util.generate_categories()

    model = Sequential()
    model.add(Conv2D(75, kernel_size=(5, 5),
                     activation='relu',
                     input_shape=(1, image_size, image_size)))
    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.2))
    model.add(Conv2D(100, (5, 5), activation='relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.2))
    model.add(Flatten())
    model.add(Dense(500, activation='relu'))
    model.add(Dropout(0.5))
    model.add(Dense(count_categories, activation='softmax'))

    model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])
    print(model.summary())
    model.fit(train_x, train_y,
              batch_size=1,
              epochs=count_epochs,
              verbose=1)

    Util.save_nn_model(model)
