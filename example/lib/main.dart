import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_gpu_image/flutter_gpu_image.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path_provider/path_provider.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const List<String> _filtersName = [
    "BokehFilter",
    "GlitchFilter",
    "VHSFilter",
    "DeuteranomalyFilter",
    "ProtanopiaFilter",
    "ProtanomalyFilter",
    "DeuteranopiaFilter",
    "TritanomalyFilter",
    "AchromatopsiaFilter",
    "AchromatomalyFilter",
    "TritanopiaFilter",
    "SketchFilter",
    "ColorFilter",
    "BilateralBlurFilter",
    "BulgeDistortionFilter",
    "ColorInvertFilter",
    "ContrastFilter",
    //"EmbossFilter",
    "FalseColorFilter",
    "HazeFilter",
    "KuwaharaFilter",
    "PixelationFilter",
    "PosterizeFilter",
    //"SepiaToneFilter",
    "SharpenFilter",
    //new com.mth2610.gpu_image.filter.GPUImageSketchFilter2",
    //"SmoothToonFilter",
    //"SolarizeFilter",
    "SwirlFilter",
    "ToonFilter",
    "VignetteFilter",
    "ZoomBlurFilter",
    "IF1977Filter",
    "IFAmaroFilter",
    "IFBrannanFilter",
    "IFEarlybirdFilter",
    "IFHefeFilter",
    "IFHudsonFilter",
    "IFInkwellFilter",
    "IFLomoFilter",
    "IFLordKelvinFilter",
    "IFNashvilleFilter",
    "IFRiseFilter",
    "IFSierraFilter",
    "IFSutroFilter",
    "IFToasterFilter",
    "IFValenciaFilter",
    "IFWaldenFilter",
    "IFXprollFilter",
  ];

  FlutterGpuImage _gpuImage = FlutterGpuImage();

  int _textureId;
  bool _isSaving;
  String _proceessedImage;
  File _srcImage;
  List<Widget> _filterButtons = [];

  @override
  void initState() {
    _gpuImage.init().then((value){
      _textureId = value;
    });
    super.initState();
    for(var element in GPU_FILTERS.values){
      _filterButtons.add(
        _buildFilterButton(element)
      );
    }
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Stack(
          children: <Widget>[
            Column(
              children: <Widget>[
                _buildPickedImage(),
                _buildImagePickerButton(),
                Expanded(
                  child: Center(
                    child: ListView(
                      scrollDirection: Axis.horizontal,
                      children: _filterButtons,
                    ),
                  ),
                ),
              ],
            ),
            _isSaving == true
            ? Center(child: CircularProgressIndicator())
            : Container()
          ],
        )
      ),
    );
  }

  Widget _buildPickedImage(){
    return _proceessedImage != null
    ? Container(
      height: 300,
      child: RotatedBox(
        quarterTurns: _gpuImage.rotation~/90,
        child: AspectRatio(
          aspectRatio: _gpuImage.width/_gpuImage.height,
          child: Texture(textureId: _textureId) ,
        ),
      ),
    )
    // ? Container(
    //   margin: EdgeInsets.all(16),
    //   height: 300.0,
    //   decoration: BoxDecoration(
    //       border: Border.all(width: 1),
    //       image: DecorationImage(
    //         image: FileImage(File(_proceessedImage)),
    //       )
    //     )
    // )
    : Container(
      margin: EdgeInsets.all(16),
      height: 300.0,
      child: _srcImage!=null
        ? null
        : Center(
          child: Container(
            child: Text("No selected image"),
          ),
        ),
      decoration: _srcImage!=null
        ? BoxDecoration(
          border: Border.all(width: 1),
          image: DecorationImage(
            image: FileImage(_srcImage),
          )
        )
        : BoxDecoration(
          border: Border.all(width: 1)
        ),
    );
  }

  Widget _buildImagePickerButton(){
    return Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          IconButton(
            icon: Icon(Icons.camera_alt),
            onPressed: ()async{
              var image = await ImagePicker.pickImage(source: ImageSource.camera);
              await _gpuImage.setInputImage(image.path);
              setState(() {
                _srcImage = image;
                _proceessedImage = null;
              });
            },
          ),
          IconButton(
            icon: Icon(Icons.folder),
            onPressed: ()async{
              var image = await ImagePicker.pickImage(source: ImageSource.gallery);
              await _gpuImage.setInputImage(image.path);
              setState(() {
                _srcImage = image;
                _proceessedImage = null;
              });
            },
          )
        ],
      ),
    );
  }

   Widget _buildFilterButton(GPU_FILTERS filter){
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: RaisedButton(
        color: Colors.blue,
        textColor: Colors.white,
        child: Text("${filter.toString()}"),
        onPressed: ()async{
          setState(() {
            _isSaving = true;
          });
          if(_srcImage!=null){
            Directory tempDir = await getTemporaryDirectory();
            String tempPath = tempDir.path; 
            // _proceessedImage = await _gpuImage.applyFilterAndSaveToFile(
            //   inputFilePath: _srcImage.path,
            //   outputFilePath: tempDir.path,
            //   filter: filter.index
            // );
            _proceessedImage = await _gpuImage.applyFilter(
              filter: filter.index
            );
            print(_proceessedImage);
            setState(() {
              _isSaving = false;
            });
          } else {
            setState(() {
              _isSaving = false;
            });
          }
        },
      ),
    );
  }
}