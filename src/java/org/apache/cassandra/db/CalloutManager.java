
public class CalloutManager
{
    private final static Logger logger_ = Logger.getLogger(CalloutManager.class); 
    private static final String extn_ = ".groovy";
    /* Used to lock the factory for creation of CalloutManager instance */
    private static Lock createLock_ = new ReentrantLock();
    /* An instance of the CalloutManager  */
    private static CalloutManager instance_;
    
    public static CalloutManager instance()
    {
        if ( instance_ == null )
        {
            CalloutManager.createLock_.lock();
            try
            {
                if ( instance_ == null )
                {
                    instance_ = new CalloutManager();
                }
            }
            finally
            {
                CalloutManager.createLock_.unlock();
            }
        }
        return instance_;
    }
    
    /* Map containing the name of callout as key and the callout script as value */
    private Map<String, CompiledScript> calloutCache_ = new HashMap<String, CompiledScript>();    
    /* The Groovy Script compiler instance */
    private Compilable compiler_;
    /* The Groovy script invokable instance */
    private Invocable invokable_;
    
    private CalloutManager()
    {
        ScriptEngineManager scriptManager = new ScriptEngineManager();
        ScriptEngine groovyEngine = scriptManager.getEngineByName("groovy");
        compiler_ = (Compilable)groovyEngine;
        invokable_ = (Invocable)groovyEngine;
    }
    
    /**
     * Compile the script and cache the compiled script.
     * @param script to be compiled
     * @throws ScriptException
     */
    private void compileAndCache(String scriptId, String script) throws ScriptException
    {
        if ( compiler_ != null )
        {
            CompiledScript compiledScript = compiler_.compile(script);
            calloutCache_.put(scriptId, compiledScript);
        }
    }
    
    /**
     * Invoked on start up to load all the stored callouts, compile
     * and cache them.
     * 
     * @throws IOException
     */
    void onStart() throws IOException
    {
    	String location = DatabaseDescriptor.getCalloutLocation();
    	if ( location == null )
    		return;
    	
        FileUtils.createDirectory(location);
        
        File[] files = new File(location).listFiles();
        
        for ( File file : files )
        {
            String f = file.getName();
            /* Get the callout name from the file */
            String callout = f.split(extn_)[0];
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            /* cache the callout after compiling it */
            try
            {
                compileAndCache(callout, new String(bytes));                    
            }
            catch ( ScriptException ex )
            {
                logger_.warn(LogUtil.throwableToString(ex));
            }
        }
    }
    
    /**
     * Store the callout in cache and write it out
     * to disk.
     * @param callout the name of the callout
     * @param script actual implementation of the callout
    */
    public void addCallout(String callout, String script) throws IOException
    {
        /* cache the script */
        /* cache the callout after compiling it */
        try
        {
            compileAndCache(callout, script);                    
        }
        catch ( ScriptException ex )
        {
            logger_.warn(LogUtil.throwableToString(ex));
        }
        /* save the script to disk */
        String scriptFile = DatabaseDescriptor.getCalloutLocation() + System.getProperty("file.separator") + callout + extn_;
        File file = new File(scriptFile);
        if ( file.exists() )
        {
            logger_.debug("Deleting the old script file ...");
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(scriptFile);
        fos.write(script.getBytes());
        fos.close();
    }
    
    /**
     * Remove the registered callout and delete the
     * script on the disk.
     * @param callout to be removed
     */
    public void removeCallout(String callout)
    {
        /* remove the script from cache */
        calloutCache_.remove(callout);
        String scriptFile = DatabaseDescriptor.getCalloutLocation() + System.getProperty("file.separator") + callout + ".grv";
        File file = new File(scriptFile);
        file.delete();
    }
    
    /**
     * Execute the specified callout.
     * @param callout to be executed.
     * @param args arguments to be passed to the callouts.
     */
    public Object executeCallout(String callout, Object ... args)
    {
        Object result = null;
        CompiledScript script = calloutCache_.get(callout);
        if ( script != null )
        {
            try
            {
                Bindings binding = new SimpleBindings();
                binding.put("args", args);
                result = script.eval(binding);
            }
            catch(ScriptException ex)
            {
                logger_.warn(LogUtil.throwableToString(ex));
            }
        }
        return result;
    }
}
