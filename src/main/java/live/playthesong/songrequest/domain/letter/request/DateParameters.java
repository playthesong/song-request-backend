package live.playthesong.songrequest.domain.letter.request;

import lombok.Getter;

import static live.playthesong.songrequest.global.constant.ValidationCondition.DAY_AGO_DEFAULT;
import static live.playthesong.songrequest.global.constant.ValidationCondition.DAY_AGO_MIN;

@Getter
public class DateParameters {

    private Integer dayAgo;

    public void setDayAgo(Integer dayAgo) {
        if (dayAgo <= DAY_AGO_MIN) {
            this.dayAgo = DAY_AGO_DEFAULT;
            return;
        }
        this.dayAgo = dayAgo;
    }
}
