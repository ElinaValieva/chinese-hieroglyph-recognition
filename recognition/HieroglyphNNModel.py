import numpy
from keras.models import Sequential
from keras.layers import Dense, Conv2D, Flatten
from keras.datasets import mnist
from keras.utils import to_categorical
from keras import backend as K

K.set_image_dim_ordering('th')


def train():
    # Configure seeds for returned resuls
    numpy.random.seed(42)

    # Loading data
    (X_train, y_train), (X_test, y_test) = mnist.load_data()

    # Reshape data to fit model
    X_train = X_train.reshape(60000, 1, 28, 28)
    X_test = X_test.reshape(10000, 1, 28, 28)

    # Set results to categorical
    y_train = to_categorical(y_train)
    y_test = to_categorical(y_test)

    # Configure model for neural network
    model = Sequential()
    model.add(Conv2D(32, (3, 3), activation='relu', input_shape=(1, 28, 28), data_format='channels_first'))
    model.add(Conv2D(32, kernel_size=3, activation='relu'))
    model.add(Flatten())
    model.add(Dense(10, activation='softmax'))

    # Compile model using accuracy as a measure of model performance
    model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

    # Train model
    model.fit(X_train, y_train, validation_data=(X_test, y_test), epochs=10)

    # Saving model to json file
    model_json = model.to_json()
    json_file = open("mnist_model.json", "w")
    json_file.write(model_json)
    json_file.close()
    model.save_weights("mnist_model.h5")
