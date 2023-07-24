package emotionalsongs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GUIUtilities {

    /**
     * TODO document
     * @param tf
     * @param maxLen
     */
    public static void forceTextInput(final TextField tf, final int maxLen) {

        tf.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue.matches(".*\\d.*")) {
                    tf.setText(newValue.replaceAll("\\d", ""));
                }else if (tf.getText().length() > maxLen) {
                    String s = tf.getText().substring(0, maxLen);
                    tf.setText(s);
                }
            }

        });
    }

        /**
         * TODO document
         * @param tf
         */
        public static void forceNumericInput(final TextField tf) {

            tf.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        tf.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });

        }

        /**
         * TODO document
         * @param tf
         * @param maxLen
         */
        public static void forceNumericInput(final TextField tf, final int maxLen) {

            tf.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        tf.setText(newValue.replaceAll("[^\\d]", ""));
                    }else if (tf.getText().length() > maxLen) {
                        String s = tf.getText().substring(0, maxLen);
                        tf.setText(s);
                    }
                }
            });

        }

    /**
     * TODO document
     * @param tf
     * @param maxLength
     */
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

        /**
         * TODO document
         * @param tf
         */
        public static void forceTextInput(final TextField tf) {

            tf.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (newValue.matches("\\d*")) {
                        tf.setText(newValue.replaceAll("[\\d]", ""));
                    }
                }
            });

        }

    protected static <T> void setErrorStyle(T node){

        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
        String mandatoryFieldMessage = "Required";

        if(node instanceof TextField) {
            ((TextField)node).setStyle(errorStyle);
            ((TextField)node).setPromptText(mandatoryFieldMessage);
        } if (node instanceof TextArea){
            ((TextArea)node).setStyle(errorStyle);
            ((TextArea)node).setPromptText(mandatoryFieldMessage);
        }

    }

    protected static <T> void setDefaultStyle(T node){
        if(node instanceof TextField) {
            ((TextField)node).setStyle(null);
            ((TextField)node).setPromptText(null);
        } if (node instanceof TextArea){
            ((TextArea)node).setStyle(null);
            ((TextArea)node).setPromptText(null);
        }
    }
}
