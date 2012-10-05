package dk.teachus.frontend.functions;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.components.list.DefaultFunctionItem;
import dk.teachus.frontend.components.list.LabelFunctionItem;

public abstract class CancelPubilBookingFunction extends LabelFunctionItem<PupilBooking> {
	private static final long serialVersionUID = 1L;

	public CancelPubilBookingFunction() {
		super(TeachUsSession.get().getString("General.cancelBooking"));
	}
	
	@Override
	public String getClickConfirmText(PupilBooking booking) {
		String confirmText = TeachUsSession.get().getString("General.confirmCancelBooking");
		confirmText = confirmText.replace("{pupilName}", booking.getPupil().getName());
		Pattern pattern = Pattern.compile(".*?(\\{date\\|([^\\}]*)\\}).*?");
		Matcher matcher = pattern.matcher(confirmText);
		while(matcher.find()) {
			String allDate = matcher.group(1);
			String dateFormat = matcher.group(2);
			String date = new SimpleDateFormat(dateFormat).format(booking.getDate().toDate());
			confirmText = confirmText.replace(allDate, date);
		}
		return confirmText;
	}
	
	@Override
	public void onEvent(PupilBooking booking) {
		BookingDAO bookingDAO = TeachUsApplication.get().getBookingDAO();
		bookingDAO.deleteBooking(booking);
		
		onBookingCancelled();
	}
	
	protected abstract void onBookingCancelled();
	
}
