package jerrydbeaverenhancer.handlers;

import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class UUIDHandler extends AbstractHandler {

	private static String UUID_REGIX = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if (part instanceof ITextEditor) {
				final ITextEditor editor = (ITextEditor) part;
				IDocumentProvider prov = editor.getDocumentProvider();
				IDocument doc = prov.getDocument(editor.getEditorInput());
				ISelection sel = editor.getSelectionProvider().getSelection();
				if (sel instanceof TextSelection) {
					final TextSelection textSel = (TextSelection) sel;
					String text = textSel.getText();
					if (Pattern.matches(UUID_REGIX, text)) {
						String newText = text.replaceAll("-", "").toUpperCase();
						doc.replace(textSel.getOffset(), textSel.getLength(), newText);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
