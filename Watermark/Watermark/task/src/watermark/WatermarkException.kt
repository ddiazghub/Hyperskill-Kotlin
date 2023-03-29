package watermark

sealed class WatermarkException(message: String) : RuntimeException(message) {
    class NoSuchFile(filename: String) : WatermarkException("The file $filename doesn't exist.")
    class WrongColors(file: String) : WatermarkException("The number of $file color components isn't 3.")
    class WrongBitCount(file: String) : WatermarkException("The $file isn't 24 or 32-bit.")
    object WrongDimensions : WatermarkException("The watermark's dimensions are larger.")
    object InvalidTransparency : WatermarkException("The transparency percentage isn't an integer number.")
    object TransparencyOutOfRange: WatermarkException("The transparency percentage is out of range.")
    object InvalidPositionMethod : WatermarkException("The position method input is invalid.")
    object InvalidPosition : WatermarkException("The position input is invalid.")
    object PositionOutOfRange : WatermarkException("The position input is out of range.")
    object InvalidOutputFormat : WatermarkException("The output file extension isn't \"jpg\" or \"png\".")
    object InvalidColor : WatermarkException("The transparency color input is invalid.")
}
