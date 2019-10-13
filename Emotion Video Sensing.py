
import io
import os
import cv2

# Imports the Google Cloud client library
from google.cloud import vision
from google.cloud.vision import types

# # Instantiates a client
# client = vision.ImageAnnotatorClient()
#
# # The name of the image file to annotate
# file_name = os.path.abspath('images\\testImage.jpg')
#
# # Loads the image into memory
# with io.open(file_name, 'rb') as image_file:
#     content = image_file.read()
#
# image = types.Image(content=content)
#
# # Performs label detection on the image file
# response = client.label_detection(image=image)
# labels = response.label_annotations
#
# print('Labels:')
# for label in labels:
#     print(label.description)


def detect_faces(path):
    """Detects faces in an image."""
    client = vision.ImageAnnotatorClient()

    with io.open(path, 'rb') as image_file:
        content = image_file.read()

    image = vision.types.Image(content=content)

    response = client.face_detection(image=image)
    faces = response.face_annotations

    # Names of likelihood from google.cloud.vision.enums
    likelihood_name = ('UNKNOWN', 'VERY_UNLIKELY', 'UNLIKELY', 'POSSIBLE',
                       'LIKELY', 'VERY_LIKELY')
    print('Faces:')

    for face in faces:
        print('anger: {}'.format(likelihood_name[face.anger_likelihood]))
        print('joy: {}'.format(likelihood_name[face.joy_likelihood]))
        print('surprise: {}'.format(likelihood_name[face.surprise_likelihood]))

        vertices = (['({},{})'.format(vertex.x, vertex.y)
                    for vertex in face.bounding_poly.vertices])

        print('face bounds: {}'.format(','.join(vertices)))


def web_cam_feed():
    cv2.namedWindow("preview")
    vc = cv2.VideoCapture(1)
    filename = 'video/image.jpg'

    if vc.isOpened():  # try to get the first frame
        rval, frame = vc.read()
    else:
        rval = False

    while rval:
        cv2.imshow("preview", frame)
        cv2.imwrite(filename, frame)
        detect_faces(filename)
        os.remove(filename)
        rval, frame = vc.read()
        key = cv2.waitKey(20)
        if key == 27:  # exit on ESC
            break
    cv2.destroyWindow("preview")


if __name__ == '__main__':
    web_cam_feed()
