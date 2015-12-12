//
//  LoginViewController.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "LoginViewController.h"
#import "MainViewController.h"
#import "SocketHelper.h"
#import "AppDelegate.h"

#if DEBUG
// Uncomment the following line to directly skip the LoginView
//# define kSkipLoginView

// Uncomment the following line to not check the server validation
# define kLoginDoNotValidate
#endif

// The key of the JSON response send by the server : { answer: true }
#define kLoginResponseKey	@"answer"

@interface LoginViewController ()

@end

@implementation LoginViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	[SocketHelper.sharedHelper pushDelegate:self];
    [loginButton setTitle:NSLocalizedString(@"LogIn", @"") forState:UIControlStateNormal];
	
#if DEBUG
	keyField.text = kServerKey;
#endif
	
#ifdef kSkipLoginView
	[SocketHelper connectSocket];
	[self nextPage];
#endif
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)nextPage {
	MainViewController * controller = [[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
	[self.navigationController pushViewController:controller animated:YES];
	loginButton.enabled = YES;
}

- (void)loginFailedWithWrongKey:(NSNumber *)wrongKey {
	NSString * message = NSLocalizedString(wrongKey.boolValue ? @"Login_ErrorMessage" : @"Connectivity_ErrorMessage", @"");
	[[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"") message:message delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
	loginButton.enabled = YES;
	loader.hidden = YES;
}

#pragma mark - IBAction methods

- (IBAction)login {
	[SocketHelper connectSocket];
	loginButton.enabled = NO;
	
#ifdef kLoginDoNotValidate
	[self nextPage];
#else
	waitingResponse = YES;
	loader.hidden = NO;

	[SocketHelper.sharedSocket sendEvent:@"authentication" withData:@{@"user_key": keyField.text} andAcknowledge:^(id argsData) {
		DPPLog(@"%@", argsData);

		waitingResponse = NO;
		loader.hidden = NO;

		NSDictionary * data = (NSDictionary *)argsData;
		NSNumber * loggedIn = [data valueForKey:kLoginResponseKey];

		// Check these lines with a working connection
		if (loggedIn.boolValue) {
			DLog(@"CORRECTLY LOGGED IN ON SERVER");
			[self nextPage];
		} else {
			[self loginFailedWithWrongKey:@YES];
		}
	}];
#endif
}

#pragma mark - SocketIO delegate methods

- (void)socketIO:(SocketIO *)socket onError:(NSError *)error {
	if (waitingResponse) {
		DLog(@"SocketIO error for connection");
		
		waitingResponse = NO;
		loader.hidden = YES;
		[self loginFailedWithWrongKey:@NO];
	}
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
