from keras.engine.saving import model_from_json

# Module for checking expected and predicted values
from util import Util

image_size = 100

if __name__ == "__main__":
    print('Starting testing ..')
    test_x = Util.generate_dataset2('test', image_size)
    count_categories = Util.get_categories_cnt()
    test_y = Util.generate_categories()
    try:
        json_file = open("../recognition/mnist_model.json", "r")
        loaded_model_json = json_file.read()
        json_file.close()
        loaded_model = model_from_json(loaded_model_json)
        loaded_model.load_weights("../recognition/mnist_model.h5")
        loaded_model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])
        scores = loaded_model.evaluate(test_x, test_y, verbose=0)
        print("Evaluation result %s: %.2f%%" % (loaded_model.metrics_names[1], scores[1] * 100))
    except FileNotFoundError:
        print('File not found error. Try to train NN')
