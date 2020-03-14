package jit.wxs.disruptor.demo.ringbuffer;

import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;

/**
 * @author jitwxs
 * @date 2020年03月14日 21:40
 */
public class UserEventTranslator {
    private RingBuffer<UserEvent> ringBuffer;

    public UserEventTranslator(RingBuffer<UserEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private EventTranslatorThreeArg<UserEvent, String, Double, UserEvent.SexEnum> translator = new EventTranslatorThreeArg<UserEvent, String, Double, UserEvent.SexEnum>() {
        @Override
        public void translateTo(UserEvent event, long sequence, String username, Double score, UserEvent.SexEnum sexEnum) {
            event.setUsername(username);
            event.setScore(score);
            event.setSexEnum(sexEnum);
            System.out.println("UserEventTranslator Send: " + event.toString());
        }
    };

    public void publish(String username, Double score, UserEvent.SexEnum sexEnum) {
        this.ringBuffer.publishEvent(translator, username, score, sexEnum);
    }
}
