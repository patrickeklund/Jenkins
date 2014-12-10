package org.jenkinsci.plugins.failnotenoughresources;
import hudson.AbortException;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.node_monitors.DiskSpaceMonitor;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;

import java.io.IOException;

/**
 * Sample {@link BuildWrapper}.
 *
 * <p>
 * This plugin is used to check resources, before a build
 * if the Resources are not met the build will fail
 *
 * <p>
 * before a build is performed, the {@link #setUp(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Patrick Eklund
 */
public class ResourceBuildWrapper extends BuildWrapper {

    private String configDiskLimitVariable;
    
    @SuppressWarnings("unused")
	private String getLongName() {
        return this.getClass().getName();
    }

    private String getSimpleName() {
        return this.getClass().getSimpleName() ;
    }

    public String getName() {
        return getSimpleName() ;
    }
    

    
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        // only got here if we didn't return false
        return true;
    }
    

    public void println(BuildListener listener, String Line1, String Line2) {
		listener.getLogger().println( String.format("%20s: %25s : [%s]", getSimpleName(), Line1 , Line2 ));
    }
    public void println(BuildListener listener, String Line1, String Line2, String Line3) {
		listener.getLogger().println( String.format("%20s: %25s : [%s] %s", getSimpleName(), Line1 , Line2, Line3));
    }
    public void println(BuildListener listener, String Line1, int Int1, String Line2) {
		listener.getLogger().println( String.format("%20s: %25s : [%d] %s", getSimpleName(), Line1 , Int1, Line2 ));
    }
    
    /**
     * This method returns the value of the configuration item configTextBox.
     *
     * The method name is bit awkward because <tt>config.jelly</tt> calls this method by the naming convention.
     **/
    public String getconfigDiskLimit() {
        return this.configDiskLimitVariable;
    }

    

    /**
     *  Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
     *  
     *  If the DataBound Constructor is used, otherwise set them with @DataBoundSetter
     */
    @DataBoundConstructor
    public ResourceBuildWrapper() {
    	/**
    	 *  Nothing implemented yet here 
    	 **/
    }

 
    /**
     * This method sets the value of the configuration item configTextBox.
     *
     * The method name is bit awkward because <tt>config.jelly</tt> calls this method by the naming convention.
     **/
    @DataBoundSetter 
    public void setconfigDiskLimit(String configDiskLimit) throws AbortException { 
    	if ( configDiskLimit.isEmpty() ) 
    		this.configDiskLimitVariable = configDiskLimit;
    	else if ( isInteger(configDiskLimit) )
    		this.configDiskLimitVariable = configDiskLimit;
    	else
    		throw new AbortException( "Please remove or set Integer value [" + configDiskLimit + "] Isn't a valid valuer");	
    } 

    
    @SuppressWarnings("rawtypes")
	@Override
    public Environment setUp( AbstractBuild build, Launcher launcher, BuildListener listener) {

    	println(listener, "Inside", "setUp");
    	return new Environment() {
		}; 
    }
    
    
    
    /**
	 * Overridden preCheckout.
	 * 
	 * Preform all checks
	 *   Sends an abort exception when to little resources
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void preCheckout(AbstractBuild build, Launcher launcher,	BuildListener listener) throws IOException, InterruptedException, AbortException {

		// Assign local variables
		String BuiltOnStr  = build.getBuiltOnStr();
		if (BuiltOnStr == "") {
			BuiltOnStr = "Master";
		}
		int ConvertedNodeDiskSpace = (int) ( DiskSpaceMonitor.DESCRIPTOR.get(build.getBuiltOn().toComputer()).size / (1024 * 1024 * 1024));
		int DiskLimitUsed = -1;
		int globalDiskLimitVariable = getDescriptor().getglobalDiskLimit();

		/* set DiskLimitUsed
		 * 
		 * Global value is forcefully set to 1, or integer value
		 * 
		 * Job configuration value is forcefully set to "" or integer value
		 */
		if ( ! configDiskLimitVariable.isEmpty() )
			DiskLimitUsed = Integer.parseInt(configDiskLimitVariable);
		else
			DiskLimitUsed = globalDiskLimitVariable;

		println(listener, "Inside", "preCheckout");
		println(listener, "Built On", BuiltOnStr);
		println(listener, "Space Left on Node", ConvertedNodeDiskSpace , "Gb" );
		println(listener, "Global Threshold", globalDiskLimitVariable, "Gb" );
		println(listener, "Job Threshold", configDiskLimitVariable, "Gb");
		println(listener, "Using Threshold", DiskLimitUsed, "Gb" );
		
		if ( ConvertedNodeDiskSpace < DiskLimitUsed )
			throw new AbortException( "Aborting Build Diskspace[" + ConvertedNodeDiskSpace + "] < Threshold [" + DiskLimitUsed +"]");
	}    


    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link DescriptorImpl}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     */
	@Extension // This indicates to Jenkins that this is an implementation of an extension point.
	public static class DescriptorImpl extends BuildWrapperDescriptor { 
    	
   
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private int globalDiskLimitVariable;

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

		@Override 
		public boolean isApplicable(final AbstractProject<?, ?> item) 
		{ 
			return true; 
		} 

		/**
         * Performs on-the-fly validation of the form
		 *   The name of the method follows the convention "doCheckXyz" where "xyz" is the name of the field you put in your view. 
		 *   The method gets invoked in response to the onchange event on HTML DOM.
		 *
		 *   The parameter name "value" is also significant. The 'throws' clause isn't.
		 *   - https://wiki.jenkins-ci.org/display/JENKINS/Form+Validation
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
		 *      
         */
        public FormValidation doCheckConfigDiskLimit(@QueryParameter String value) throws IOException, ServletException {
        	
        	//load the global parameters.
        	super.load();
        	
        	if ( value.isEmpty() )
       			return FormValidation.warning("Unless set, the global value [" + getglobalDiskLimit() + "] will be used");
        	else if ( ! isInteger(value) )
                return FormValidation.error("Please: Only use digits");
            return FormValidation.ok();
        }
        public FormValidation doCheckGlobalDiskLimit(@QueryParameter String value) throws IOException, ServletException {
			// This is how you include a value for a form check
        	if ( value.isEmpty() )
                return FormValidation.error("Please: Enter digit value");
        	if ( ! isInteger(value))
                return FormValidation.error("Please: Only enter digits");
            return FormValidation.ok();
        }

        @SuppressWarnings("rawtypes")
        public boolean isApplicable( Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         **/
        public String getDisplayName() {
            return ResourceBuildWrapper.class.getSimpleName().toString() + " : " + ResourceBuildWrapper.class.getName().toString();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
        	globalDiskLimitVariable = formData.getInt("globalDiskLimit");
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }


       
        /**
         * This method returns the value of the configuration item globalTextBox.
         *
         * The method name is bit awkward because <tt>global.jelly</tt> calls this method by the naming convention.
         **/
        public int getglobalDiskLimit() {
            return this.globalDiskLimitVariable;
        }

    }
}

