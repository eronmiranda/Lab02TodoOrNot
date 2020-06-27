package ca.nait.dmit2504.lab02todoornot;

public class ListItem {
    private long mId;
    private String mListItemName;
    private String mDate;
    private String mIsComplete;
    private String mTitleId;
    private String ListTitle;

    public String getListTitle() {
        return ListTitle;
    }

    public void setListTitle(String listTitle) {
        ListTitle = listTitle;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getListItemName() {
        return mListItemName;
    }

    public void setListItemName(String listItemName) {
        mListItemName = listItemName;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getIsComplete() {
        return mIsComplete;
    }

    public void setIsComplete(String isComplete) {
        mIsComplete = isComplete;
    }

    public String getTitleId() {
        return mTitleId;
    }

    public void setTitleId(String titleId) {
        mTitleId = titleId;
    }
}
