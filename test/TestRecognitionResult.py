from recognition import HieroglyphRecognition


def test_1():
    assert HieroglyphRecognition.recognize('../test_data/0.png') == 11
    assert HieroglyphRecognition.recognize('../test_data/16.png') == 11
    assert HieroglyphRecognition.recognize('../test_data/17.png') == 11
    assert HieroglyphRecognition.recognize('../test_data/18.png') == 14
    assert HieroglyphRecognition.recognize('../test_data/1.png') == 17
    assert HieroglyphRecognition.recognize('../test_data/3.png') == 15
    assert HieroglyphRecognition.recognize('../test_data/15.png') == 15
    assert HieroglyphRecognition.recognize('../test_data/20.png') == 16
    assert HieroglyphRecognition.recognize('../test_data/19.png') == 17


def test_2():
    assert HieroglyphRecognition.recognize('../test_data/14.png') == 1
    assert HieroglyphRecognition.recognize('../test_data/4.png') == 1
    assert HieroglyphRecognition.recognize('../test_data/2.png') == 2
    assert HieroglyphRecognition.recognize('../test_data/5.png') == 2
    assert HieroglyphRecognition.recognize('../test_data/7.png') == 2
    assert HieroglyphRecognition.recognize('../test_data/9.png') == 2
    assert HieroglyphRecognition.recognize('../test_data/11.png') == 2
    assert HieroglyphRecognition.recognize('../test_data/6.png') == 3
    assert HieroglyphRecognition.recognize('../test_data/8.png') == 4
    assert HieroglyphRecognition.recognize('../test_data/10.png') == 5
    assert HieroglyphRecognition.recognize('../test_data/12.png') == 6
    assert HieroglyphRecognition.recognize('../test_data/13.png') == 7


def test_3():
    assert HieroglyphRecognition.recognize('../test_data/19.png') == 17
    assert HieroglyphRecognition.recognize('../test_data/20.png') == 16


if __name__ == "__main__":
    print('Starting test')
    test_1()
    test_2()
    test_3()
    print('All test have passed')
