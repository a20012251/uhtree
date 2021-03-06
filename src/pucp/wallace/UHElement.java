package pucp.wallace;

/*
 * Clase que almacena un value un su contador de accesos. La clase es lock-free.
 */
public class UHElement {
	private int count = 0;
	private Object value;

	public UHElement(Object value) {
		this.value = value;
	}

	/*
	 * Incrementa en uno el contador.
	 */
	public void incr() {
		count++;
	}

	/*
	 * Develve el valor del contador.
	 */
	public int getCount() {
		return count;
	}

	/*
	 * Devuelve el value asociado.
	 */
	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof UHElement) {
			UHElement uhElement = (UHElement) other;
			return value.equals(uhElement.getValue());
		} else
			return value.equals(other);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
