package scriptservice.uhc.loupgarou.classes;

import scriptservice.uhc.loupgarou.enums.camps;

public class winResult {
    private final boolean _isWin;
    private final String _winMessage;
    private final camps _winCamp;

    // public "set"
    public winResult(boolean isWin, String winMessage, camps winCamp) {
        this._isWin = isWin;
        this._winMessage = winMessage;
        this._winCamp = winCamp;
    }

    // "get"
    public boolean getWin() {
        return _isWin;
    }

    public String getMessage() {
        return _winMessage;
    }

    public camps getCamp() {
        return _winCamp;
    }

    // duplicates
    public String getWinMessage() {
        return getMessage();
    }

    public boolean isWin() {
        return getWin();
    }
}
