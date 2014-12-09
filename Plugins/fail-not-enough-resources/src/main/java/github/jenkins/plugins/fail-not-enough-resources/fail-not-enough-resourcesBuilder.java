package github.com.patriceklund.jenkins.plugins.Template;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.List;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link TemplateBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class TemplateBuilder extends Builder {

    private boolean configCheckBoxVariable;
    private String configTextBoxVariable;
    
    private String getLongName() {
        return this.getClass().getName();
    }

    private String getSimpleName() {
        return this.getClass().getSimpleName() ;
    }

    public String getName() {
        return getSimpleName() ;
    }

/*    protected String getCause(AbstractBuild build) { 
        List<Cause> localCauseList = build.getCauses();
        
        for (String cause : build.getCauses() ) {
        	
        }
        for String item in localCauseList {
        
        }
        return this.cause; 
    } 
*/
    

    /**
     * This method returns the value of the configuration item configCheckBox.
     *
     * The method name is bit awkward because <tt>config.jelly</tt> calls this method by the naming convention.
     **/
    public boolean getconfigCheckBox() {
        return this.configCheckBoxVariable;
    }

    /**
     * This method returns the value of the configuration item configTextBox.
     *
     * The method name is bit awkward because <tt>config.jelly</tt> calls this method by the naming convention.
     **/
    public String getconfigTextBox() {
        return this.configTextBoxVariable;
    }

    

    /**
     *  Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
     *  
     *  If the DataBound Constructor is used, otherwise set them with @DataBoundSetter
     */
    @DataBoundConstructor
    public TemplateBuilder() {
    	/**
    	 *  Nothing implemented yet here 
    	 **/
    }

    /**
     * This method sets the value of the configuration item configCheckBox.
     *
     * The method name is bit awkward because <tt>config.jelly</tt> calls this method by the naming convention.
     **/
    @DataBoundSetter 
    public void setconfigCheckBox(boolean configCheckBox) { 
    	this.configCheckBoxVariable = configCheckBox; 
	} 

    /**
     * This method sets the value of the configuration item configTextBox.
     *
     * The method name is bit awkward because <tt>config.jelly</tt> calls this method by the naming convention.
     **/
    @DataBoundSetter 
    public void setconfigTextBox(String configTextBox) { 
    	this.configTextBoxVariable = configTextBox; 
	} 
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean perform( AbstractBuild build, Launcher launcher, BuildListener listener) {

		// This is where you 'build' the project.
		listener.getLogger().println( String.format("%20s: %25s : [%s]", getSimpleName(), "Start of Plugin", getLongName() ));
		listener.getLogger().println( String.format("%20s: ", getSimpleName() ));
		
		// This shows how you can consult build items of the builder
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Display name",   build.getDisplayName() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Description",    build.getDescription() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Duration", 	   build.getDurationString() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Built on",       build.getBuiltOnStr() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Build Nr",       build.getNumber() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Build URL",      build.getUrl() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Status URL",     build.getBuildStatusUrl() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Status Summary", build.getBuildStatusSummary() ));
		
		// Get all possible built causes
		@SuppressWarnings("unchecked")
		List<Cause> causes=build.getCauses();
		for (  Cause cause : causes) {
			listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Build", "Cause", cause.getShortDescription() ));
		}

		// This shows how you can consult the global configuration of the builder
		listener.getLogger().println( String.format("%20s: ", getSimpleName() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Global", "Check Box", getDescriptor().getglobalCheckBox()));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Global", "Text Box",  getDescriptor().getglobalTextBox()));


		// This shows how you can consult the configuration of the builder
		listener.getLogger().println( String.format("%20s: ", getSimpleName() ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Config", "Check Box", configCheckBoxVariable ));
		listener.getLogger().println( String.format("%20s: %-10s %15s : [%s]", getSimpleName(), "Config", "Text Box",  configTextBoxVariable ));

		listener.getLogger().println( String.format("%20s: ", getSimpleName() ));
		listener.getLogger().println( String.format("%20s: %25s : [%s]", getSimpleName(), "End of Plugin", getLongName() ));
		
        return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link TemplateBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/TemplateBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private boolean globalCheckBoxVariable;
        private String globalTextBoxVariable;

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
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
        public FormValidation doCheckConfigTextBox(@QueryParameter String value, @QueryParameter boolean configCheckBox) throws IOException, ServletException {
			// This is how you include 2 values for a form check
			if (value.length() == 0)
				return FormValidation.error("Please enter something, CheckBox:[" + configCheckBox + "]");
            if (value.length() < 4)
                return FormValidation.warning("Needs to be at least 4 characters long, CheckBox:[" + configCheckBox + "]");
            if (value.length() > 10)
                return FormValidation.warning("To long to be maximum 10 characters long, CheckBox:[" + configCheckBox + "]");
            return FormValidation.ok("CheckBox:[" + configCheckBox + "]");
        }
        public FormValidation doCheckGlobalTextBox(@QueryParameter String value) throws IOException, ServletException {
			// This is how you include a value for a form check
            if (value.length() == 0)
                return FormValidation.error("Please enter something");
            if (value.length() < 4)
                return FormValidation.warning("Needs to be at least 4 characters long");
            if (value.length() > 10)
                return FormValidation.warning("To long to be maximum 10 characters long");
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
            return TemplateBuilder.class.getSimpleName().toString() + " : " + TemplateBuilder.class.getName().toString();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            globalCheckBoxVariable = formData.getBoolean("globalCheckBox");
            globalTextBoxVariable = formData.getString("globalTextBox");
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }


        /**
         * This method returns the value of the configuration item globalCheckBox.
         *
         * The method name is bit awkward because <tt>global.jelly</tt> calls this method by the naming convention.
         **/
        public boolean getglobalCheckBox() {
            return this.globalCheckBoxVariable;
        }

        
        /**
         * This method returns the value of the configuration item globalTextBox.
         *
         * The method name is bit awkward because <tt>global.jelly</tt> calls this method by the naming convention.
         **/
        public String getglobalTextBox() {
            return this.globalTextBoxVariable;
        }

    }
}

