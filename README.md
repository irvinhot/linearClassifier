# Simple linear classifier to use with MNIST database

**Author: Irvin Hot (irwinhot[at]gmail.com)**

This is a simple linear classifier for optical character recognition (digits 0-9).
You need to download datasets from: http://yann.lecun.com/exdb/mnist/ 
Put all the files in folder where you compiled the classes. 

Classifier is using mini-batch gradient descent. 
You can change number of classes you are training, learning rate 
and batch size in testing class using "train" method.

Total error based on training set using mini-batch gradient descent is 9.7%. 

It is possible to reduce the error with Batch (Vanilla) gradient descent 
but it requires a lot of time to train the classifier.

