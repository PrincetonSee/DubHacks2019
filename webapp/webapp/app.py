from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/carecam')
def carecam():
    return 'Welcome to Carecam! This page is still a work in progress.'

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')

