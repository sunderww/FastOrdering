//
//  MainViewController.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "MainViewController.h"
#import "NotificationViewController.h"
#import "TablePlanViewController.h"
#import "MenuViewController.h"
#import "AboutViewController.h"
#import "SocketIO.h"
#import "AppDelegate.h"

@interface MainViewController ()

@end

@implementation MainViewController

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

    [self connectToSocketIO];
    panelShown = NO;
    panelView.hidden = NO;
    overlay.alpha = 0;
    titleLabel.text = NSLocalizedString(@"mainPageTitle", @"");
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Helper methods

- (void)connectToSocketIO {
    // Handshake
//    NSString * URLString = [NSString stringWithFormat:@"http://%@:%d/dish", kSocketIOHost, kSocketIOPort];
//    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:URLString] cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:10];
//    
//    [request setHTTPMethod: @"GET"];
//    
//    NSError *requestError;
//    NSURLResponse *urlResponse = nil;
//    NSData *response1 = [NSURLConnection sendSynchronousRequest:request returningResponse:&urlResponse error:&requestError];
//    DPPLog(@"%@", [[NSString alloc] initWithData:response1 encoding:NSUTF8StringEncoding]);
    
    socket = [[SocketIO alloc] initWithDelegate:self];
    [socket connectToHost:kSocketIOHost onPort:kSocketIOPort withParams:@{@"cookie":@"12345"}];
    
    [socket get:@"/dish" withData:nil callback:^(id response) {
        DPPLog(@"%@", response);
    }];
    
//    [socket connectToHost:kSocketIOHost onPort:kSocketIOPort];
//    [socket sendEvent:@"/dish/read" withData:@{}];
//    [socket sendMessage:@"/dish/read"];
//
//    NSString *url = @"/dish/read";
//    NSDictionary *params = @{@"url" : url};
//    [socket sendEvent:@"get" withData:params];
}

- (void)loadDatabase {
    DPPLog(@"Should try to load the correct db with coredata");
    
}

- (void)hidePanel {
    if (panelShown)
        [self showPanel];
}

#pragma mark - IBAction methods

- (IBAction)showPanel {
    panelShown = !panelShown;
    
    [UIView animateWithDuration:0.4 animations:^{
        CGRect frame = panelView.frame;
        frame.origin.x += frame.size.width * (panelShown ? 1 : -1);
        panelView.frame = frame;
        overlay.alpha = panelShown ? 1 : 0;
    }];
}

- (IBAction)buttonClicked:(id)sender {
    [controller.view removeFromSuperview];

    if (sender == mainButton) {
        controller = nil;
        titleLabel.text = NSLocalizedString(@"mainPageTitle", @"");
    } else if (sender == tableButton) {
        controller = [[TablePlanViewController alloc] initWithNibName:@"TablePlanView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"tablePageTitle", @"");
    } else if (sender == menuButton) {
        controller = [[MenuViewController alloc] initWithNibName:@"MenuView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"menuPageTitle", @"");
    } else if (sender == historyButton) {
        controller = nil;
        titleLabel.text = NSLocalizedString(@"historyPageTitle", @"");
    } else if (sender == notificationsButton) {
        controller = [[NotificationViewController alloc] initWithNibName:@"NotificationView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"notificationPageTitle", @"");
    } else if (sender == aboutButton) {
        controller = [[AboutViewController alloc] initWithNibName:@"AboutView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"aboutPageTitle", @"");
    } else {
       DLog(@"UNKNOWN BUTTON %@", sender);
        titleLabel.text = @"";
    }

    [self hidePanel];
    CGRect frame = centralView.frame;
    frame.origin = CGPointZero;
    controller.view.frame = frame;
    [centralView addSubview:controller.view];
}

- (IBAction)logOut {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - SocketIO delegate methods

- (void)socketIO:(SocketIO *)socket didReceiveMessage:(SocketIOPacket *)packet {
    PPLog(@"MESSAGE %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveJSON:(SocketIOPacket *)packet {
    PPLog(@"JSON %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveEvent:(SocketIOPacket *)packet {
    PPLog(@"EVENT %@", packet);
}

- (void)socketIO:(SocketIO *)socket didSendMessage:(SocketIOPacket *)packet {
    PPLog(@"SENT %@", packet);
}

- (void)socketIODidConnect:(SocketIO *)socket {
    PPLog(@"CONNECT");
}

- (void)socketIODidDisconnect:(SocketIO *)socket disconnectedWithError:(NSError *)error {
    PPLog(@"DECO %@", error);
}

- (void)socketIO:(SocketIO *)socket onError:(NSError *)error {
    PPLog(@"%@", error);
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
