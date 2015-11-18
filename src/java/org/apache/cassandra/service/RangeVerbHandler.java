
public class RangeVerbHandler implements IVerbHandler
{
    public void doVerb(Message message)
    {
        List<String> keys;
        RangeCommand command = RangeCommand.read(message);
        Table table = Table.open(command.table);
        keys = table.getKeyRange(command.startWith, command.stopAt, command.maxResults);

        Message response = new RangeReply(keys).getReply(message);
        MessagingService.getMessagingInstance().sendOneWay(response, message.getFrom());
    }
}
