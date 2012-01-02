function initFramework( )
%Initializes the local work environment. 
%   Detailed explanation goes here

    % check if the user has a aim-work folder. 
    userdir = ''; %#ok<NASGU>
    if ispc
        userdir = getenv('USERPROFILE');
    else 
        userdir = getenv('HOME');
    end
    [status, msg, msgid] = mkdir(userdir, 'aim-work');
    
    if(status==0)
        fprintf('Creating aim-work folder resulted in a message: %s\n', msg);
    end
    workDir = [userdir filesep 'aim-work'];
    
    % 
    version='1.0-SNAPSHOT-jar-with-dependencies';
    module='aim-framework';
    
    fileName = [module '-' version '.jar'];
    
    host='http://ahbuild:8090/';
    
    urlString = [host fileName];
    % download it to the work directory
    urlwrite(urlString,[workDir filesep fileName]);
    
    fprintf('Please ensure that you put the following classpath entry into your classpath.txt:\n%s\n', [workDir filesep fileName])
    
    
end

