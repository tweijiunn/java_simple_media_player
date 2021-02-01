/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package media.player;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author tweij
 */
public class FXMLDocumentController implements Initializable {
    
    
    
    private MediaPlayer mediaPlayer;
    private String filepath;
    
    
    
    @FXML
    private MediaView mv;
    
    @FXML
    private Slider slider;
    
    @FXML
    private Slider seekSlider;
    
    private Duration totalTime;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser file= new FileChooser();
        FileChooser.ExtensionFilter filter= new FileChooser.ExtensionFilter("Select a file (*.mp4)","*.mp4");
            file.getExtensionFilters().add(filter);
            File f= file.showOpenDialog(null);
            filepath= f.toURI().toString();
            
            if(filepath!=null){
                
                Media media = new Media(filepath);
                mediaPlayer = new MediaPlayer(media);
                mv.setMediaPlayer(mediaPlayer);
                
                
                    DoubleProperty width = mv.fitWidthProperty();
                    DoubleProperty height = mv.fitHeightProperty();
                    
                    //set video to attach to the full screen
                    width.bind(Bindings.selectDouble(mv.sceneProperty(),"width"));
                    height.bind(Bindings.selectDouble(mv.sceneProperty(),"height"));
                    
                    //get system sound volume
                    slider.setValue(mediaPlayer.getVolume()*100);
                    slider.valueProperty().addListener(new InvalidationListener(){
                    @Override
                    public void invalidated(Observable observable){
                        mediaPlayer.setVolume(slider.getValue()/100);
                    }
                    });
                    
                    mediaPlayer.setOnReady(new Runnable() {

                        @Override
                        public void run() {
                            
                            totalTime = media.getDuration();
                            
                        }
                    });

                    mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>(){
                        @Override
                        public void changed(ObservableValue<? extends Duration> observable,Duration oldValue, Duration newValue){
                            seekSlider.setValue(newValue.divide(totalTime.toSeconds()).toSeconds()*100.0);

                        }
                    });
                    
                    seekSlider.setOnMouseClicked((new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                       mediaPlayer.seek(totalTime.multiply(seekSlider.getValue()/100.0));
                    }

                    }));
                
                //Play the video
                mediaPlayer.play();
            }
    }
    
    @FXML
    private void playVideo(ActionEvent event){
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }
    
    @FXML
    private void pauseVideo(ActionEvent event){
        mediaPlayer.pause();
    }

    @FXML
    private void stopVideo(ActionEvent event){
        mediaPlayer.stop();
    }

    @FXML
    private void fastVideo(ActionEvent event){
        mediaPlayer.setRate(1.5);
    }

    @FXML
    private void fasterVideo(ActionEvent event){
        mediaPlayer.setRate(10);
    }

    @FXML
    private void slowVideo(ActionEvent event){
        mediaPlayer.setRate(0.75);
    }

    @FXML
    private void slowerVideo(ActionEvent event){
        mediaPlayer.setRate(0.5);
    }

    @FXML
    private void exitVideo(ActionEvent event){
        System.exit(0);
    }


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
