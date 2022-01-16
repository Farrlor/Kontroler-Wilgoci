package it.farrlor.zsi.zsi_project;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {

    /*
    Inteligentne stabilizowanie parametru wilgotności w pomieszczeniu zamkniętym.

    Aplikacja prezentująca zastosowanie reguł logiki rozmytej w dosyć prymitywnym kontrolerze wilgoci.
     */

    SwitchButton switchButton = new SwitchButton();

    String nazwa_wilgocenia;
    double ustawienie_wetting;
    double humidity = 70.0;
    double temperatura = 23.5;
    //
    double hum_sucho = 0.0;
    double hum_optymalnie = 0.0;
    double hum_mokro = 0.0;

    double hum_procent = 0.0;
    String hum_wynik = "brak_stanu";
    //
    double temp_bzimno = 0.0;
    double temp_zimno = 0.0;
    double temp_optymalnie = 0.0;
    double temp_goraco = 0.0;
    double temp_bgoraco = 0.0;

    double temp_procent = 0.0;
    String temp_wynik = "brak_stanu";
    //

    @FXML
    private TextArea statusLeft;

    @FXML
    private TextArea statusRight;

    @FXML
    public ToggleButton toggleButton;

    @FXML
    public Label clock;

    @FXML
    private ProgressBar progressLeft;

    @FXML
    private ProgressBar progressRight;

    @FXML
    private TextField tempText;
    @FXML
    private TextField humText;

    public void InitController() {
        //symulacja
        refleshFrame();
        //rozmycie
        wyznaczWetting();
        //clock
        statusRight.appendText("**ZEGAR DZIAŁA**\n");
        //togglebutton
        toggleButton.setGraphic(switchButton);
        statusRight.appendText("**TOGGLE BUTTON DZIAŁA**\n");
        nightTime();
        //statusBary
        setStatusLeft();
        setStatusRight();
        statusRight.appendText("**PASKI STATUSU URUCHOMIONE**\n");
        //progressBary
        setProgressLeft();
        setProgressRight();
        //
        tempText.setText(String.valueOf(Double.parseDouble(String.valueOf(temperatura))));
        humText.setText(String.valueOf(Double.parseDouble(String.valueOf(humidity))));
    }
    public void blokada() {
        if (tempText.getText().isEmpty() == true || tempText.getText().isBlank() == true) {
            tempText.setText(String.valueOf(Double.parseDouble(String.valueOf(temperatura))));
            //System.out.println("Pusty tempText string, ustawiam: " + Double.parseDouble(String.valueOf(temperatura)));
        }

        if (humText.getText().isEmpty() == true) {
            humText.setText(String.valueOf(Double.parseDouble(String.valueOf(humidity))));
        }
    }

    public void nightTime() {
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        // JEŻELI TRUE TO NOC
                        if (switchButton.state == true) {
                            ZonedDateTime start = Instant.now().atZone(ZoneId.systemDefault()).plus(12, ChronoUnit.HOURS);
                            String startTimestamp = start.format(dateFormatter);
                            clock.setText("Aktualny czas: " + startTimestamp);
                        // JEŻELI FALSE TO DZIEŃ
                        } else if (switchButton.state == false) {
                            ZonedDateTime start = Instant.now().atZone(ZoneId.systemDefault());
                            String startTimestamp = start.format(dateFormatter);
                            clock.setText("Aktualny czas: " + startTimestamp);
                        }
                    }
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public void exitApp() {
        System.exit(0);
    }

    public void reloadWetting() {
        wyznaczWetting();
    }

    public void reloadStatusLeft() {
        //System.out.print("**RELOAD LEWEGO STATUS BARU**\n");
        statusLeft.setText(null);
        setStatusLeft();
    }

    public void reloadProgressLeft() {
        progressLeft.setProgress(0);
        setProgressLeft();
        //System.out.print("Przeładowano lewy progress bar\n");
    }

    public void reloadProgressRight() {
        progressRight.setProgress(0);
        setProgressRight();
        //System.out.print("Przeładowano prawy progress bar\n");
    }

    public void setStatusLeft() {
        statusLeft.setEditable(false);
        statusLeft.setText(null);
        statusRight.scrollTopProperty();

        // ** TEMPERATURA

        statusLeft.appendText("Temperatura: " + temperatura + "°C || TERM: " + temp_procent*100 + "% || REZULTAT: " + temp_wynik + "\n");

        // ** WILGOTNOŚĆ

        statusLeft.appendText("Wilgotność: " + humidity + "% || TERM: " + hum_procent*100 + "% || REZULTAT: " + hum_wynik + "\n");

        // ** WETTING

        statusLeft.appendText("Zwilzanie: " + nazwa_wilgocenia + " [" + ustawienie_wetting + "]\n");

    }

    public void setStatusRight() {
        statusRight.setEditable(false);
        statusRight.scrollTopProperty();
        statusRight.setText("# Terminal statusu:\n");
        statusRight.appendText("**PRAWY STATUS BAR DZIAŁA**\n");
    }

    public void setProgressLeft() {
        if (temp_wynik == "bardzo zimno") {
            progressLeft.setProgress(0.10);
        } else if (temp_wynik == "zimno") {
            progressLeft.setProgress(0.25);
        } else if (temp_wynik == "optymalnie") {
            progressLeft.setProgress(0.50);
        } else if (temp_wynik == "gorąco") {
            progressLeft.setProgress(0.75);
        } else if (temp_wynik == "bardzo gorąco") {
            progressLeft.setProgress(1.0);
        } else {
            progressRight.setProgress(0);
            System.out.println("E: Nieznany status progressbaru lewego");
        }
    }

    public void setProgressRight() {
        if (nazwa_wilgocenia == "moczenie") {
            progressRight.setProgress(0.33);
        } else if (nazwa_wilgocenia == "optymalnie") {
            progressRight.setProgress(0.66);
        } else if (nazwa_wilgocenia == "suszenie") {
            progressRight.setProgress(1.0);
        } else {
            progressRight.setProgress(0);
            System.out.println("E: Nieznany status progressbaru prawego");
        }
    }

    public void setTemperature() {

        double temp_liczba = Double.parseDouble(tempText.getText());

        if (temp_liczba >= 27.0) {
            statusRight.appendText("$ TEMPERATURA POZA ZAKRESEM\n");
            temperatura = 20.0;
        } else if (temp_liczba <= 20.0) {
            statusRight.appendText("$ TEMPERATURA POZA ZAKRESEM\n");
            temperatura = 20.0;
        } else {
            temperatura = temp_liczba;
            wyznaczTemperature();
            reloadWetting();
            reloadProgressLeft();
            reloadProgressRight();
            reloadStatusLeft();
        }
    }
    public void setWilgotnosc() {
        double hum_liczba = Double.parseDouble(humText.getText());

        if (hum_liczba > 100.0) {
            statusRight.appendText("** WILGOTNOŚĆ POZA ZAKRESEM **\n$ Zakres to 0 -> 100\n");
            humidity = 0.0;
        } else if (hum_liczba < 0.0) {
            statusRight.appendText("** WILGOTNOŚĆ POZA ZAKRESEM **\n$ Zakres to 0 -> 100\n");
            humidity = 0.0;
        } else {
            humidity = hum_liczba;
            wyznaczWilgotnosc();
            reloadWetting();
            reloadProgressLeft();
            reloadProgressRight();
            reloadStatusLeft();
        }
    }
    // =======================METODY LOGIKI=============================== //
    // ==================WYZNACZANIE TEMPERATURY========================== //

    public void wyznaczTemperature() {
        temp_bzimno = 0.0;
        temp_zimno = 0.0;
        temp_optymalnie = 0.0;
        temp_goraco = 0.0;
        temp_bgoraco = 0.0;

        // b.zimno
        if (temperatura >= 20.0 && temperatura <= 22.03) {
            temp_bzimno = ((22.03 - Double.parseDouble(tempText.getText()))/22.03);
        }else{
            temp_bzimno = 0.0;
        }
        // zimno
        if (temperatura >= 21.86 && temperatura <= 22.36) {
            temp_zimno = ((Double.parseDouble(tempText.getText())) / 22.36);
        }else if (Double.parseDouble(tempText.getText()) >= 22.36 && Double.parseDouble(tempText.getText()) <= 23.06) {
            temp_zimno = ((23.06 - Double.parseDouble(tempText.getText())) / 22.36);
        }else{
            temp_zimno = 0.0;
        }
        //optymalnie
        if (temperatura >= 23.0 && temperatura <= 23.5) {
            temp_optymalnie = Double.parseDouble(tempText.getText()) / 23.5;
        }else if (temperatura >= 23.5 && temperatura <= 24.0) {
            temp_optymalnie = ((24.0 - Double.parseDouble(tempText.getText())) / 23.5);
        }else{
            temp_optymalnie = 0.0;
        }
        //goraco
        if (temperatura >= 23.9 && temperatura <= 24.06) {
            temp_goraco = ((Double.parseDouble(tempText.getText()))/24.06);
        }else if (temperatura >= 24.06 && temperatura <=24.97) {
            temp_goraco = ((24.97 - Double.parseDouble(tempText.getText())) / 24.06);
        }else{
            temp_goraco = 0.0;
        }
        //bgoraco
        if (temperatura >= 24.87 && temperatura <= 27.00) {
            temp_bgoraco = (((Double.parseDouble(tempText.getText()) - 24.87))/24.87);
        }else{
            temp_bgoraco = 0.0;
        }

        double sumaTemp = temp_bzimno + temp_zimno + temp_optymalnie + temp_goraco + temp_bgoraco;

        double max_1 = Math.max(temp_bzimno/sumaTemp, temp_zimno/sumaTemp);
        double max_2 = Math.max(max_1, temp_optymalnie/sumaTemp);
        double max_3 = Math.max(max_2, temp_goraco/sumaTemp);
        double final_max = Math.max(max_3, temp_bgoraco/sumaTemp);

        String wynik = "brak_wyniku";

        if (final_max == temp_bzimno/sumaTemp) {
            wynik = "bardzo zimno";
        }else if (final_max == temp_zimno/sumaTemp) {
            wynik = "zimno";
        }else if (final_max == temp_optymalnie/sumaTemp) {
            wynik = "optymalnie";
        }else if (final_max == temp_goraco/sumaTemp) {
            wynik = "gorąco";
        }else if (final_max == temp_bgoraco/sumaTemp) {
            wynik = "bardzo gorąco";
        }else{
            wynik = "błąd krytyczny";
        }
        temp_procent = final_max;
        temp_wynik = wynik;
        //System.out.print("**TEMPERATURA**  MAX: " + final_max + " <> WYNIK:" + wynik + "\n");
    }
    // ================================================================== //
    // ====================WYZNACZANIE WILGOTNOSCI======================== //

    public void wyznaczWilgotnosc() {
        // wilgotnosc sucho
        if(humidity >= 0.0 && humidity <= 44.25) {
            hum_sucho = (44.25-humidity)/44.25;
        }else{
            hum_sucho = 0.0;
        }
        //optymalna
        if(humidity >= 38.4 && humidity <= 45.08) {
            hum_optymalnie = (humidity/45.08);
        }else if (humidity >= 45.08 && humidity <= 51.9) {
            hum_optymalnie = (51.9 - humidity)/45.08;
        }else{
            hum_optymalnie = 0.0;
        }
        //mokro
        if(humidity >= 50.0 && humidity <= 100.0) {
            hum_mokro = (humidity-50.0)/50.0;
        }else {
            hum_mokro = 0.0;
        }

        double sumaWilg = hum_mokro + hum_optymalnie + hum_sucho;

        double max_1 = Math.max(hum_sucho/sumaWilg, hum_optymalnie/sumaWilg);
        double final_max = Math.max(max_1, hum_mokro/sumaWilg);

        String wynik = "brak_stanu";

        if (final_max == hum_sucho/sumaWilg) {
            wynik = "sucho";
        }else if (final_max == hum_optymalnie/sumaWilg) {
            wynik = "optymalnie";
        }else if (final_max == hum_mokro/sumaWilg) {
            wynik = "mokro";
        }else {
            wynik = "błąd krytyczny";
        }
        hum_procent = final_max;
        hum_wynik = wynik;

    }
    // =============================================================== //
    // ====================WYZNACZANIE WETTING======================== //
    //rozmycie
    public void wyznaczWetting() {

            double R1;
            double R2;
            double R3;
            double R4;
            double R5;
            double R6;
            double R7;
            double R8;
            double R9;
            double R10;
            double R11;
            double R12;
            double R13;
            double R14;
            double R15;

            //moczenie
            R1 = Math.min(temp_bzimno, hum_sucho);
            //moczenie
            R2 = Math.min(temp_bzimno, hum_optymalnie);
            //moczenie
            R3 = Math.min(temp_bzimno, hum_mokro);
            //moczenie
            R4 = Math.min(temp_zimno, hum_sucho);
            //optymalnie
            R5 = Math.min(temp_zimno, hum_optymalnie);
            //optymalnie
            R6 = Math.min(temp_zimno, hum_mokro);
            //optymalnie
            R7 = Math.min(temp_optymalnie, hum_sucho);
            //optymalnie
            R8 = Math.min(temp_optymalnie, hum_optymalnie);
            //suszenie
            R9 = Math.min(temp_optymalnie, hum_mokro);
            //optymalnie
            R10 = Math.min(temp_goraco, hum_sucho);
            //suszenie
            R11 = Math.min(temp_goraco, hum_optymalnie);
            //suszenie
            R12 = Math.min(temp_goraco, hum_mokro);
            //suszenie
            R13 = Math.min(temp_bgoraco, hum_sucho);
            //suszenie
            R14 = Math.min(temp_bgoraco, hum_optymalnie);
            //suszenie
            R15 = Math.min(temp_bgoraco, hum_mokro);

            double max_1 = Math.max(R1,R2);
            double max_2 = Math.max(max_1,R3);
            double max_3 = Math.max(max_2,R4);
            double max_4 = Math.max(max_3,R5);
            double max_5 = Math.max(max_4,R6);
            double max_6 = Math.max(max_5,R7);
            double max_7 = Math.max(max_6,R8);
            double max_8 = Math.max(max_7,R9);
            double max_9 = Math.max(max_8,R10);
            double max_10 = Math.max(max_9,R11);
            double max_11 = Math.max(max_10,R12);
            double max_12 = Math.max(max_11,R13);
            double max_13 = Math.max(max_12,R14);
            double final_max = Math.max(max_13,R15);

            if (final_max == R1 || final_max == R2 || final_max == R3 || final_max == R4) {
                System.out.println("final_max: " + final_max);
                ustawienie_wetting = (final_max - 4.0) / (-(4.0/3.0));
                nazwa_wilgocenia = "moczenie";
            }else if (final_max == R5 || final_max == R6 || final_max == R7 || final_max == R8 || final_max == R10) {
                System.out.println("final_max: " + final_max);
                ustawienie_wetting = (final_max - 4.0) / (-(2.0));
                nazwa_wilgocenia = "optymalnie";
            }else if (final_max == R9 || final_max == R11 || final_max == R12 || final_max == R13 || final_max == R14 || final_max == R15) {
                ustawienie_wetting = (final_max - 2.0) / (-(4.0/3.0));
                nazwa_wilgocenia = "suszenie";
            }else{
                ustawienie_wetting = 0;
                nazwa_wilgocenia = "błąd krytyczny";
            }
    }
    // =============================================================== //
    // ========================SYMULACJA============================== //
    public void refleshFrame() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                wyznaczTemperature();
                wyznaczWilgotnosc();
                wyznaczWetting();
                blokada();
                if (nazwa_wilgocenia == "suszenie" && humidity >= 1) {
                    humidity = (humidity - 2.0);
                    setStatusLeft();
                    setProgressLeft();
                    setProgressRight();
                }else if(nazwa_wilgocenia == "optymalnie") {
                    humidity = (humidity+0);
                    setStatusLeft();
                    setProgressLeft();
                    setProgressRight();
                }else if(nazwa_wilgocenia == "moczenie" && humidity <= 99) {
                    humidity = (humidity + 2.0);
                    setStatusLeft();
                    setProgressLeft();
                    setProgressRight();
                }else{
                    humidity = (humidity+0);
                    //System.out.println("E: Błąd krytyczny symulacji");
                }
            }
        }, 2000, 2000);
    }
    // =============================================================== //
}