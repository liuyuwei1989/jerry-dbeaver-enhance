package jerrydbeaverenhancer.handlers;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.swt.widgets.Composite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Button;

public class OracleDataFunctionHanlder extends AbstractHandler {

	protected Shell shlOrcaleQuickFunction;

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlOrcaleQuickFunction.open();
		shlOrcaleQuickFunction.layout();

		while (!shlOrcaleQuickFunction.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlOrcaleQuickFunction = new Shell();
		shlOrcaleQuickFunction.setSize(292, 308);
		shlOrcaleQuickFunction.setText("Orcale Quick Function");

		Composite composite = new Composite(shlOrcaleQuickFunction, SWT.NONE);
		composite.setBounds(0, 0, 275, 275);

		DateTime calendarView = new DateTime(composite, SWT.BORDER | SWT.CALENDAR);
		calendarView.setBounds(28, 10, 219, 160);

		DateTime time = new DateTime(composite, SWT.BORDER | SWT.TIME);
		time.setBounds(152, 176, 95, 24);

		doActionDependsOnSelectedText((textSel, doc) -> {
			try {
				String text = textSel.getText();
				Date date = simpleDateFormat.parse(text);
				Calendar calendarTmp = Calendar.getInstance();
				calendarTmp.setTime(date);
				calendarView.setYear(calendarTmp.get(Calendar.YEAR));
				calendarView.setMonth(calendarTmp.get(Calendar.MONTH));
				calendarView.setDay(calendarTmp.get(Calendar.DAY_OF_MONTH));
				time.setTime(0, 0, 0);
			} catch (Exception e) {
				// TODO: handle exception
			}
		});

		Button btnTodate = new Button(composite, SWT.NONE);
		btnTodate.setBounds(152, 239, 95, 27);
		btnTodate.setText("TO_DATE");
		btnTodate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doActionDependsOnSelectedText((textSel, doc) -> {
					int year = calendarView.getYear();
					int month = calendarView.getMonth();
					int day = calendarView.getDay();
					String to_date = String.format("TO_DATE('%d-%02d-%02d','YYYY-MM-DD')", year, month + 1, day);
					try {
						doc.replace(textSel.getOffset(), textSel.getLength(), to_date);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					shlOrcaleQuickFunction.dispose();
				});
			}
		});

		Button btnTodatetime = new Button(composite, SWT.NONE);
		btnTodatetime.setBounds(152, 206, 95, 27);
		btnTodatetime.setText("TO_DATETIME");
		btnTodatetime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doActionDependsOnSelectedText((textSel, doc) -> {
					int year = calendarView.getYear();
					int month = calendarView.getMonth();
					int day = calendarView.getDay();
					int hours = calendarView.getHours();
					int minutes = calendarView.getMinutes();
					int seconds = calendarView.getSeconds();
					String to_date = String.format("TO_DATE('%d-%02d-%02d %02d:%02d:%02d','YYYY-MM-DD HH24:MI:SS')", year, month + 1, day, hours,minutes,seconds);
					try {
						doc.replace(textSel.getOffset(), textSel.getLength(), to_date);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					shlOrcaleQuickFunction.dispose();
				});
			}
		});

		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setBounds(28, 239, 80, 27);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlOrcaleQuickFunction.dispose();
			}
		});

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			OracleDataFunctionHanlder window = new OracleDataFunctionHanlder();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void doActionDependsOnSelectedText(BiConsumer<TextSelection, IDocument> function) {
		try {
			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if (part instanceof ITextEditor) {
				final ITextEditor editor = (ITextEditor) part;
				IDocumentProvider prov = editor.getDocumentProvider();
				IDocument doc = prov.getDocument(editor.getEditorInput());
				ISelection sel = editor.getSelectionProvider().getSelection();
				if (sel instanceof TextSelection) {
					final TextSelection textSel = (TextSelection) sel;
					function.accept(textSel, doc);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
