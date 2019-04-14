from recognition import HieroglyphRecognition


# Module for checking expected and predicted values

def test():
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
    assert HieroglyphRecognition.recognize('../test_data/28.png') == 8
    assert HieroglyphRecognition.recognize('../test_data/31.png') == 8
    assert HieroglyphRecognition.recognize('../test_data/0.png') == 11
    assert HieroglyphRecognition.recognize('../test_data/16.png') == 11
    assert HieroglyphRecognition.recognize('../test_data/17.png') == 11
    assert HieroglyphRecognition.recognize('../test_data/18.png') == 14
    assert HieroglyphRecognition.recognize('../test_data/1.png') == 17
    assert HieroglyphRecognition.recognize('../test_data/3.png') == 15
    assert HieroglyphRecognition.recognize('../test_data/15.png') == 15
    assert HieroglyphRecognition.recognize('../test_data/20.png') == 16
    assert HieroglyphRecognition.recognize('../test_data/19.png') == 17
    assert HieroglyphRecognition.recognize('../test_data/25.png') == 18
    assert HieroglyphRecognition.recognize('../test_data/26.png') == 19
    assert HieroglyphRecognition.recognize('../test_data/30.png') == 19
    assert HieroglyphRecognition.recognize('../test_data/23.png') == 20
    assert HieroglyphRecognition.recognize('../test_data/22.png') == 21
    assert HieroglyphRecognition.recognize('../test_data/21.png') == 22
    assert HieroglyphRecognition.recognize('../test_data/27.png') == 23
    assert HieroglyphRecognition.recognize('../test_data/29.png') == 24
    assert HieroglyphRecognition.recognize('../test_data/32.png') == 25
    assert HieroglyphRecognition.recognize('../test_data/33.png') == 26
    assert HieroglyphRecognition.recognize('../test_data/34.png') == 27
    assert HieroglyphRecognition.recognize('../test_data/35.png') == 28
    assert HieroglyphRecognition.recognize('../test_data/36.png') == 29
    assert HieroglyphRecognition.recognize('../test_data/24.png') == 30
    assert HieroglyphRecognition.recognize('../test_data/37.png') == 30


if __name__ == "__main__":
    print('Starting testing ..')
    test()
    print('All test have passed!')
