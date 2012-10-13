package dk.teachus.frontend.components.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

/**
 * Used Jasny Bootstrap Input mask plugin to add character restriction support to text fields.
 * 
 * @see http://jasny.github.com/bootstrap/javascript.html#inputmask
 */
public class InputMaskBehavior extends Behavior {
	private static final long serialVersionUID = 1L;
	
	private final String inputMask;
	private final char placeholder;
	
	/**
	 * Add input mask support to the &lt;input type="text"&gt; field. It will use '_' as placeholder.
	 * 
	 * @param inputMask
	 *            A string of formatting and literal characters, defining the input mask. See below table for mask
	 *            characters:
	 *            <table>
	 *            <tr>
	 *            <th>Character</th>
	 *            <th>Description</th>
	 *            </tr>
	 *            <tr>
	 *            <td>9</td>
	 *            <td>Number</td>
	 *            </tr>
	 *            <tr>
	 *            <td>a</td>
	 *            <td>Letter</td>
	 *            </tr>
	 *            <tr>
	 *            <td>?</td>
	 *            <td>Alphanumeric</td>
	 *            </tr>
	 *            <tr>
	 *            <td>*</td>
	 *            <td>Any character</td>
	 *            </tr>
	 *            </table>
	 */
	public InputMaskBehavior(String inputMask) {
		this(inputMask, '_');
	}
	
	/**
	 * Add input mask support to the &lt;input type="text"&gt; field.
	 * 
	 * @param inputMask
	 *            A string of formatting and literal characters, defining the input mask. See below table for mask
	 *            characters:
	 *            <table>
	 *            <tr>
	 *            <th>Character</th>
	 *            <th>Description</th>
	 *            </tr>
	 *            <tr>
	 *            <td>9</td>
	 *            <td>Number</td>
	 *            </tr>
	 *            <tr>
	 *            <td>a</td>
	 *            <td>Letter</td>
	 *            </tr>
	 *            <tr>
	 *            <td>?</td>
	 *            <td>Alphanumeric</td>
	 *            </tr>
	 *            <tr>
	 *            <td>*</td>
	 *            <td>Any character</td>
	 *            </tr>
	 *            </table>
	 * @param placeholder
	 *            The character that is displayed where something needs to be typed.
	 */
	public InputMaskBehavior(String inputMask, char placeholder) {
		this.inputMask = inputMask;
		this.placeholder = placeholder;
	}
	
	@Override
	public void bind(Component component) {
		component.add(AttributeModifier.replace("data-mask", inputMask));
		component.add(AttributeModifier.replace("data-placeholder", placeholder));
	}
	
}
