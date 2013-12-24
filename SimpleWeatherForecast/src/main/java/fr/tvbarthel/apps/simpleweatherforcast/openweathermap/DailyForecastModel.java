package fr.tvbarthel.apps.simpleweatherforcast.openweathermap;


import android.os.Parcel;
import android.os.Parcelable;

public class DailyForecastModel implements Parcelable {

	private long mDateTime;
	private String mDescription;
	private Double mTemperature;
	private int mHumidity;

	public DailyForecastModel() {
	}

	public DailyForecastModel(Parcel in) {
		readFromParcel(in);
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public int getHumidity() {
		return mHumidity;
	}

	public void setHumidity(int humidity) {
		mHumidity = humidity;
	}

	public long getDateTime() {
		return mDateTime;
	}

	public void setDateTime(long dateTime) {
		mDateTime = dateTime;
	}

	public Double getTemperature() {
		return mTemperature;
	}

	public void setTemperature(Double temperature) {
		mTemperature = temperature;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mDateTime);
		dest.writeString(mDescription);
		dest.writeInt(mHumidity);
		dest.writeDouble(mTemperature);
	}

	private void readFromParcel(Parcel in) {
		mDateTime = in.readLong();
		mDescription = in.readString();
		mHumidity = in.readInt();
		mTemperature = in.readDouble();
	}

	public static final Parcelable.Creator<DailyForecastModel> CREATOR = new Parcelable.Creator<DailyForecastModel>() {
		@Override
		public DailyForecastModel createFromParcel(Parcel source) {
			return new DailyForecastModel(source);
		}

		@Override
		public DailyForecastModel[] newArray(int size) {
			return new DailyForecastModel[size];
		}
	};

}
