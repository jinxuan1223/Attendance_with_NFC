package AwNFC;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Shear;
import javafx.util.Duration;

import java.util.Calendar;

public class Clock {
    private Calendar calendar = Calendar.getInstance();
    private Digit[] digits;
    private Timeline delayTimeline, secondTimeline;

    public Clock(Color onColor, Color offColor, AnchorPane pane) {
        // create effect for on LEDs
        Glow onEffect = new Glow(1.7f);
        onEffect.setInput(new InnerShadow());

        // create effect for on dot LEDs
        Glow onDotEffect = new Glow(1.7f);
        onDotEffect.setInput(new InnerShadow(5,Color.BLACK));

        // create digits
        digits = new Digit[7];
        for (int i = 0; i < 6; i++) {
            Digit digit = new Digit(onColor, offColor);
            digit.setLayoutX(i * 80 + ((i + 1) % 2) * 20);
            digits[i] = digit;
            pane.getChildren().add(digit);
        }

        // create dots
        Group dots = new Group(
                new Circle(80 + 54 + 20, 44, 6, onColor),
                new Circle(80 + 54 + 17, 64, 6, onColor),
                new Circle((80 * 3) + 54 + 20, 44, 6, onColor),
                new Circle((80 * 3) + 54 + 17, 64, 6, onColor));
        //dots.setEffect(onDotEffect);
        pane.getChildren().add(dots);
        // update digits to current time and start timer to update every second
        refreshClocks();
        play();
    }

    private void refreshClocks() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        digits[0].showNumber(hours / 10);
        digits[1].showNumber(hours % 10);
        digits[2].showNumber(minutes / 10);
        digits[3].showNumber(minutes % 10);
        digits[4].showNumber(seconds / 10);
        digits[5].showNumber(seconds % 10);
    }

    public void play() {
        // wait till start of next second then start a timeline to call refreshClocks() every second
        delayTimeline = new Timeline();
        delayTimeline.getKeyFrames().add(
                new KeyFrame(new Duration(1000 - (System.currentTimeMillis() % 1000)), new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent event) {
                        if (secondTimeline != null) {
                            secondTimeline.stop();
                        }
                        secondTimeline = new Timeline();
                        secondTimeline.setCycleCount(Timeline.INDEFINITE);
                        secondTimeline.getKeyFrames().add(
                                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent event) {
                                        refreshClocks();
                                    }
                                }));
                        secondTimeline.play();
                    }
                })
        );
        delayTimeline.play();
    }


    /**
     * Simple 7 segment LED style digit. It supports the numbers 0 through 9.
     */
    public static final class Digit extends Parent {
        private static final boolean[][] DIGIT_COMBINATIONS = new boolean[][]{
                new boolean[]{true, false, true, true, true, true, true},       // 0
                new boolean[]{false, false, false, false, true, false, true},   // 1
                new boolean[]{true, true, true, false, true, true, false},      // 2
                new boolean[]{true, true, true, false, true, false, true},      // 3
                new boolean[]{false, true, false, true, true, false, true},     // 4
                new boolean[]{true, true, true, true, false, false, true},      // 5
                new boolean[]{true, true, true, true, false, true, true},       // 6
                new boolean[]{true, false, false, false, true, false, true},    // 7
                new boolean[]{true, true, true, true, true, true, true},        // 8
                new boolean[]{true, true, true, true, true, false, true}};      // 9
        private final Polygon[] polygons = new Polygon[]{
                new Polygon(2, 0, 52, 0, 42, 10, 12, 10),
                new Polygon(12, 49, 42, 49, 52, 54, 42, 59, 12f, 59f, 2f, 54f),
                new Polygon(12, 98, 42, 98, 52, 108, 2, 108),
                new Polygon(0, 2, 10, 12, 10, 47, 0, 52),
                new Polygon(44, 12, 54, 2, 54, 52, 44, 47),
                new Polygon(0, 56, 10, 61, 10, 96, 0, 106),
                new Polygon(44, 61, 54, 56, 54, 106, 44, 96)};
        private final Color onColor;
        private final Color offColor;

        public Digit(Color onColor, Color offColor) {
            this.onColor = onColor;
            this.offColor = offColor;
            getChildren().addAll(polygons);
            getTransforms().add(new Shear(-0.1,0));
            showNumber(0);
        }

        public void showNumber(Integer num) {
            if (num < 0 || num > 9) num = 0; // default to 0 for non-valid numbers
            for (int i = 0; i < 7; i++) {
                polygons[i].setFill(DIGIT_COMBINATIONS[num][i] ? onColor : offColor);
            }
        }
    }


}
