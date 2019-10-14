package ch.beerpro.presentation.modal;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import java.util.function.Consumer;

import ch.beerpro.presentation.details.DetailsActivity;

public class ModalInputNumberDialog {

    public static void readUserInputNumber(Context context, String title, String message, Consumer<Integer> success, Runnable cancel) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(title);
        alert.setMessage(message);

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton("Ok", (dialog, whichButton) -> {

            int amount;

            try {
                String inputAsString = input.getText().toString();
                amount = Integer.parseInt(inputAsString);
                success.accept(amount);
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
                cancel.run();
                return;
            }

        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            cancel.run();
        });

        alert.show();

    }

}
