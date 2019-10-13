
import io
import os
import cv2
import firebase_admin

# Imports the Google Cloud client library
from google.cloud import vision
# from google.cloud import storage
from google.cloud.vision import types
from playsound import playsound
from firebase_admin import credentials
from firebase_admin import storage


def retrieve_audio_file(n):
    filename = 'CareCamAudio' + str(n) + '.wav'
    bucket = storage.bucket('carecam-593ba.appspot.com')
    blob = bucket.blob(filename)

    with open(filename, 'w') as file_obj:
        blob.download_to_filename(filename)
        file_obj.close()

    print('retrieved.')

    # 'bucket' is an object defined in the google-cloud-storage Python library.
    # See https://googlecloudplatform.github.io/google-cloud-python/latest/storage/buckets.html
    # for more details.
    return filename


def detect_faces(path, num):
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
        if face.joy_likelihood == 4 or face.joy_likelihood == 5:
            filename = retrieve_audio_file(num)
            playsound(filename)


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
    cred = credentials.Certificate('Desktop/keys/carecam-593ba-firebase-adminsdk-gr35d-a83ad42f4e.json')
    firebase_admin.initialize_app(cred)
    web_cam_feed()
